package com.baekgwa.account.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import domain.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.account.domain
 * FileName    : AccountHistory
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String sagaId;

	@Column(nullable = false)
	private String accountNumber;

	@Enumerated(EnumType.STRING)
	private TransactionType type;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column
	private String message; // optional

	@Builder(access = AccessLevel.PRIVATE)
	public AccountHistory(String sagaId, String accountNumber, TransactionType type, BigDecimal amount,
		LocalDateTime createdAt, String message) {
		this.sagaId = sagaId;
		this.accountNumber = accountNumber;
		this.type = type;
		this.amount = amount;
		this.createdAt = createdAt;
		this.message = message;
	}

	public static AccountHistory of(String sagaId, String accountNumber, TransactionType type,
		BigDecimal amount, String message) {
		return AccountHistory
			.builder()
			.sagaId(sagaId)
			.accountNumber(accountNumber)
			.type(type)
			.amount(amount)
			.createdAt(LocalDateTime.now())
			.message(message)
			.build();
	}
}
