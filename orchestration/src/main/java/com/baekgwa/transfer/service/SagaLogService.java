package com.baekgwa.transfer.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baekgwa.transfer.domain.TransferSaga;
import com.baekgwa.transfer.repository.TransferSagaRepository;

import domain.SagaStatus;
import domain.TransferStep;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.transfer.service
 * FileName    : SagaLogService
 * Author      : Baekgwa
 * Date        : 25. 12. 10.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 10.     Baekgwa               Initial creation
 */
@Service
@RequiredArgsConstructor
public class SagaLogService {

	private final TransferSagaRepository transferSagaRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public TransferSaga updateSagaStep(String sagaId, SagaStatus status, TransferStep step) {
		TransferSaga saga = transferSagaRepository.findById(sagaId)
			.orElseThrow(() -> new RuntimeException("Saga not found"));

		saga.updateStatusStep(status, step);
		return transferSagaRepository.save(saga);
	}

	/**
	 * 초기 Saga 시작 기록
	 * 메인 트랜잭션과 별개로 항상 저장되어 관리되어야 함
	 * @param fromAccountNumber 출금 계좌
	 * @param toAccountNumber 입금 계좌
	 * @param amount 금액
	 * @return 생성된 계좌이체 Saga
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public TransferSaga initSaga(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
		String generatedSagaId = UUID.randomUUID().toString();
		TransferSaga saga = TransferSaga.newTransferSaga(
			generatedSagaId,
			fromAccountNumber,
			toAccountNumber,
			amount
		);

		return transferSagaRepository.save(saga);
	}
}
