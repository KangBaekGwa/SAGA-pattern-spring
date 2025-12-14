package com.baekgwa.account;

import java.math.BigDecimal;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baekgwa.account.domain.Account;
import com.baekgwa.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : com.baekgwa.account
 * FileName    : DataInitializer
 * Author      : Baekgwa
 * Date        : 25. 12. 11.
 * Description : 초기 Account 정보 등록 및 금액 추가를 위한 이벤트 리스너
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 11.     Baekgwa               Initial creation
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

	private final AccountRepository accountRepository;

	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void init() {
		if (accountRepository.findAll().isEmpty()) {
			Account account1 = Account.of("111-222", BigDecimal.valueOf(10_000));
			Account account2 = Account.of("333-444", BigDecimal.valueOf(20_000));

			accountRepository.save(account1);
			accountRepository.save(account2);
		}
	}
}
