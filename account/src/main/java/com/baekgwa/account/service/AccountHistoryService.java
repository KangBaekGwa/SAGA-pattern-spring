package com.baekgwa.account.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baekgwa.account.domain.AccountHistory;
import com.baekgwa.account.repository.AccountHistoryRepository;

import domain.TransactionType;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.account.service
 * FileName    : AccountHistoryService
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
public class AccountHistoryService {

	private final AccountHistoryRepository accountHistoryRepository;

	/**
	 * 실패되어 롤백되더라고, 계좌에 무언가 처리를 하려고 한 행위 자체는 물리적으로 항시 기록되어있어야 함
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void newAccountHistory(
		String sagaId, String accountNumber, TransactionType type, BigDecimal amount, String message
	) {
		AccountHistory accountHistory = AccountHistory.of(sagaId, accountNumber, type, amount, message);
		accountHistoryRepository.save(accountHistory);
	}
}
