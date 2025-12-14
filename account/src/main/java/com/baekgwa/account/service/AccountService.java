package com.baekgwa.account.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baekgwa.account.domain.Account;
import com.baekgwa.account.repository.AccountRepository;

import domain.SagaStatus;
import domain.TransactionType;
import dto.AccountTransferDto;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.account.service
 * FileName    : AccountService
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final AccountHistoryService accountHistoryService;

	/**
	 * 계좌에서 금액 확인 후 차감 처리
	 * @param request
	 * @return
	 */
	@Transactional
	public AccountTransferDto.AccountWithdrawResponse withdraw(
		AccountTransferDto.AccountWithdrawRequest request
	) {
		// 1. 요청 접수 로그 (성공/실패 여부와 관계없이 기록)
		accountHistoryService.newAccountHistory(
			request.getSagaId(),
			request.getAccountNumber(),
			TransactionType.DEBIT,
			request.getAmount(),
			"출금 요청 접수"
		);

		Account findAccount;
		try {
			// 2. [Lock] 비관적 락을 걸고 계좌 조회
			findAccount = accountRepository.findByAccountNumberWithLock(request.getAccountNumber())
				.orElseThrow(() -> new IllegalArgumentException("계좌번호가 존재하지 않습니다."));

			// 3. 잔액 확인
			if (findAccount.getBalance().compareTo(request.getAmount()) < 0) {
				throw new IllegalArgumentException("잔액이 부족합니다.");
			}

			// 4. 차감 처리
			findAccount.withdrawBalance(request.getAmount());

		} catch (Exception e) {
			// 5. [Exception & Audit] 실패 시, 실패 로그를 남기고 예외를 다시 던짐
			// Transactional이 롤백되더라도 accountHistoryService는 REQUIRES_NEW라 기록됨
			String failMessage = e.getMessage();

			accountHistoryService.newAccountHistory(
				request.getSagaId(),
				request.getAccountNumber(),
				TransactionType.ROLLBACK_DEBIT,
				request.getAmount(),
				"실패: " + failMessage
			);

			throw e;
		}

		// 6. 응답 생성
		return new AccountTransferDto.AccountWithdrawResponse(
			request.getSagaId(),
			SagaStatus.COMPLETED.toString(),
			request.getAmount() + "원 출금 완료"
		);
	}

	/**
	 * 계좌에 금액 입금 처리
	 * @param request
	 * @return
	 */
	@Transactional
	public AccountTransferDto.AccountDepositResponse deposit(
		AccountTransferDto.AccountDepositRequest request
	) {
		// 1. 요청 접수 로그 저장
		accountHistoryService.newAccountHistory(
			request.getSagaId(),
			request.getAccountNumber(),
			TransactionType.CREDIT,
			request.getAmount(),
			"입금 요청 접수"
		);

		Account findAccount;
		try {
			// 1. 입금 계좌 정보 확인 및 lock
			findAccount = accountRepository.findByAccountNumberWithLock(request.getAccountNumber())
				.orElseThrow(() -> new IllegalArgumentException("계좌번호가 존재하지 않습니다."));

			// 2. 계좌 입금 처리
			findAccount.depositBalance(request.getAmount());

		} catch (Exception e) {
			// 3. [Exception & Audit] 실패 시, 실패 로그를 남기고 예외를 다시 던짐
			String failMessage = e.getMessage();

			accountHistoryService.newAccountHistory(
				request.getSagaId(),
				request.getAccountNumber(),
				TransactionType.ROLLBACK_CREDIT,
				request.getAmount(),
				"실패: " + failMessage
			);

			throw e;
		}

		// 4. 응답 생성
		return new AccountTransferDto.AccountDepositResponse(
			request.getSagaId(),
			SagaStatus.COMPLETED.toString(),
			request.getAmount() + "원 입금 되었습니다."
		);
	}

	/**
	 * 출금된 금액만큼 다시 계좌에 입금 처리
	 * @param request
	 * @return
	 */
	@Transactional
	public AccountTransferDto.AccountWithdrawRollbackResponse withdrawRollback(
		AccountTransferDto.AccountWithdrawRollbackRequest request
	) {
		// 1. 롤백 기록
		accountHistoryService.newAccountHistory(
			request.getSagaId(),
			request.getAccountNumber(),
			TransactionType.ROLLBACK_DEBIT,
			request.getAmount(),
			"출금 취소되었습니다."
		);

		Account findAccount;
		try {
			// 2. 입금 계좌 정보 확인 및 lock
			findAccount = accountRepository.findByAccountNumberWithLock(request.getAccountNumber())
				.orElseThrow(() -> new IllegalArgumentException("계좌번호가 존재하지 않습니다."));

			// 3. 계좌 입금 처리
			findAccount.depositBalance(request.getAmount());
		} catch (Exception e) {
			// 4. [Exception & Audit] 실패 시, 실패 로그를 남기고 예외를 다시 던짐
			String failMessage = e.getMessage();

			accountHistoryService.newAccountHistory(
				request.getSagaId(),
				request.getAccountNumber(),
				TransactionType.ROLLBACK_CREDIT,
				request.getAmount(),
				"실패: " + failMessage
			);

			throw e;
		}

		return new AccountTransferDto.AccountWithdrawRollbackResponse(
			request.getSagaId(),
			SagaStatus.COMPLETED.toString(),
			request.getAmount() + "이체에 실패하여, 이체 금액이 반환되었습니다."
		);
	}
}
