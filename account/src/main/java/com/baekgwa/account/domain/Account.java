package com.baekgwa.account.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.account.domain
 * FileName    : Account
 * Author      : Baekgwa
 * Date        : 25. 12. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 8.     Baekgwa               Initial creation
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String accountNumber;

	@Column(nullable = false)
	private BigDecimal balance;

	@Builder(access = AccessLevel.PRIVATE)
	private Account(String accountNumber, BigDecimal balance) {
		this.accountNumber = accountNumber;
		this.balance = balance;
	}

	public static Account of(String accountNumber, BigDecimal balance) {
		return Account
			.builder()
			.accountNumber(accountNumber)
			.balance(balance)
			.build();
	}

	public void withdrawBalance(BigDecimal amount) {
		this.balance = this.balance.subtract(amount);
	}

	public void depositBalance(BigDecimal amount) {
		this.balance = this.balance.add(amount);
	}
}
