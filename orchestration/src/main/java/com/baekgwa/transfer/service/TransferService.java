package com.baekgwa.transfer.service;

import java.math.BigDecimal;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.baekgwa.transfer.client.AccountClient;
import com.baekgwa.transfer.domain.TransferSaga;

import domain.SagaStatus;
import domain.SendStatus;
import domain.TransferStep;
import dto.AccountTransferDto;
import dto.TransferDto;
import event.SagaEvent;
import event.TopicList;
import event.TransferEvent;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PackageName : com.baekgwa.transfer.service
 * FileName    : TransferService
 * Author      : Baekgwa
 * Date        : 25. 12. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 8.     Baekgwa               Initial creation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

	private final AccountClient accountClient;
	private final SagaLogService sagaLogService;
	private final KafkaTemplate<String, SagaEvent> kafkaTemplate;

	public TransferDto.TransferResponse transfer(
		TransferDto.TransferRequest request
	) {
		// 1. 계좌 이체 saga 시작
		// 별도의 트랜잭션에서 저장. 해당 saga log 는 롤백되어도 기록됨
		TransferSaga saveSaga = sagaLogService.initSaga(
			request.getFromAccountNumber(),
			request.getToAccountNumber(),
			request.getAmount()
		);

		// 2. 계좌 잔액 확인 및 출금 처리
		AccountTransferDto.AccountWithdrawResponse withdrawResponse;
		try {
			// 2-1. 현재 출금 상태 업데이트
			sagaLogService.updateSagaStep(saveSaga.getSagaId(), SagaStatus.STARTED, TransferStep.WITHDRAW_START);

			// 2-2. 출금 처리 (동기)
			withdrawResponse = accountClient.withdraw(
				new AccountTransferDto.AccountWithdrawRequest(
					saveSaga.getSagaId(),
					request.getFromAccountNumber(),
					request.getAmount())
			);

			// 2-3. 출금 완료 후, 업데이트 처리
			sagaLogService.updateSagaStep(saveSaga.getSagaId(), SagaStatus.PENDING, TransferStep.WITHDRAW_END);

			// 2-4. 로깅 처리
			log.info("출금 처리 완료 response : {}", withdrawResponse.toString());
		} catch (Exception e) {
			// 2-5. 출금 오류 시 처리
			// 출금 자체가 실패하면 롤백할 것이 없음.
			String errorMessage = extractErrorMessage(e);
			sagaLogService.updateSagaStep(saveSaga.getSagaId(), SagaStatus.FAILED, TransferStep.FINISH);
			log.warn("[Saga: {}] 출금 실패 - {}", saveSaga.getSagaId(), errorMessage);

			// 2-6. 출금 오류 시, 알림 발송
			sendNotification(
				saveSaga.getSagaId(),
				request.getFromAccountNumber(),
				"이체 실패: " + errorMessage,
				SendStatus.FAILED
			);

			// 2-7. 실패 결과 return
			return TransferDto.TransferResponse.of(saveSaga.getSagaId(), SagaStatus.FAILED.name(), errorMessage);
		}

		// 3. 보내는 계좌로 송금 처리
		AccountTransferDto.AccountDepositResponse depositResponse;
		try {
			// 3-1. 현재 입금 상태 업데이트
			sagaLogService.updateSagaStep(saveSaga.getSagaId(), SagaStatus.STARTED, TransferStep.DEPOSIT_START);

			// 3-2. 송금 처리 (동기)
			depositResponse = accountClient.deposit(
				new AccountTransferDto.AccountDepositRequest(
					saveSaga.getSagaId(),
					request.getToAccountNumber(),
					request.getAmount())
			);

			// 3-3. 송금 완료 후, 업데이트 처리
			sagaLogService.updateSagaStep(saveSaga.getSagaId(), SagaStatus.PENDING, TransferStep.DEPOSIT_END);
			log.info("입금 처리 완료 response : {}", depositResponse.toString());
		} catch (Exception e) {
			// 3-4. 입금 실패 시, 트랜잭션 롤백 실행 필요.
			// 이전 출금된 계좌에서 다시 입금 처리
			String errorMessage = extractErrorMessage(e);
			log.warn("[Saga: {}] 입금 실패 - {}", saveSaga.getSagaId(), errorMessage);

			withdrawRollback(saveSaga.getSagaId(), request.getFromAccountNumber(), request.getAmount());

			// 3-5. 이체 실패 알림 발송.
			sendNotification(
				saveSaga.getSagaId(),
				request.getFromAccountNumber(),
				"이체 실패(입금오류): " + errorMessage,
				SendStatus.FAILED
			);

			// 3-6. 실패 결과 return
			return TransferDto.TransferResponse.of(saveSaga.getSagaId(), SagaStatus.FAILED.name(), errorMessage);
		}

		// 4. 알림 처리
		// 출금/입금 한 사람 두분에게 모두 알림 처리
		// 비동기로 이벤트 발송 (kafka)
		try {
			sendNotification(
				saveSaga.getSagaId(),
				request.getFromAccountNumber(),
				withdrawResponse.getMessage(),
				SendStatus.SENT
			);
			sendNotification(
				saveSaga.getSagaId(),
				request.getToAccountNumber(),
				depositResponse.getMessage(),
				SendStatus.SENT
			);
		} catch (Exception e) {
			log.warn("[Saga: {}] 알림 발송 실패 : {}", saveSaga.getSagaId(), e.getStackTrace());
		}

		// 5. saga 업데이트
		sagaLogService.updateSagaStep(saveSaga.getSagaId(), SagaStatus.COMPLETED, TransferStep.FINISH);

		// 6. 완료 처리
		return TransferDto.TransferResponse.of(saveSaga.getSagaId(), SagaStatus.COMPLETED.name(), "출금 처리 완료");
	}

	/**
	 * 계좌이체 출금 Rollback 로직
	 * @param sagaId
	 * @param rollbackAccountNumber
	 * @param amount
	 */
	private void withdrawRollback(String sagaId, String rollbackAccountNumber, BigDecimal amount) {
		try {
			// 1. Rollback 상태 업데이트
			sagaLogService.updateSagaStep(sagaId, SagaStatus.STARTED, TransferStep.COMPENSATE);

			// 2. Rollback 보상 로직 실행 (동기)
			accountClient.withdrawRollback(
				new AccountTransferDto.AccountWithdrawRollbackRequest(
					sagaId,
					rollbackAccountNumber,
					amount)
			);

			sagaLogService.updateSagaStep(sagaId, SagaStatus.ABORTED, TransferStep.FINISH);
		} catch (Exception e) {
			// 실패 시, 복구 동작이 실패함.
			// 명백하게 개발자/운영자 측에서 확인 후 수동 처리가 필요할 수 있음.
			// 즉, 계좌이체 했는데 돈만 없어진 상황
			log.error("[Saga: {}] 이체 롤백 실패 - 에러: {}", sagaId, e.getMessage());

			sagaLogService.updateSagaStep(sagaId, SagaStatus.FAILED, TransferStep.FINISH);
		}
	}

	/**
	 * 알림 발송 메서드
	 * 비동기적으로 알림 서비스에 알림 요청
	 * @param sagaId
	 * @param recipientAccountNumber
	 * @param message
	 * @param status
	 */
	private void sendNotification(String sagaId, String recipientAccountNumber, String message, SendStatus status) {
		TransferEvent.TransferNotificationEvent notificationEvent = TransferEvent.TransferNotificationEvent.of(
			sagaId, recipientAccountNumber, message, status);

		kafkaTemplate.send(TopicList.TRANSFER_NOTIFICATION_TOPIC, sagaId, notificationEvent);
		log.info("[kafka] 알림 전송 완료. {}, {}", message, status);
	}

	/**
	 * FeignException인 경우 Body(실제 메시지)를 추출하고,
	 * 일반 Exception인 경우 getMessage()를 반환하는 헬퍼 메소드
	 */
	private String extractErrorMessage(Exception e) {
		if (e instanceof FeignException feignException) {
			// AccountService가 String Body로 보냈으므로 그대로 꺼내면 됨
			// 값이 없으면 status code라도 리턴
			String content = feignException.contentUTF8();
			return (content != null && !content.isEmpty()) ? content : "Remote Error: " + feignException.status();
		}
		return e.getMessage();
	}
}
