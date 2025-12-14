package com.baekgwa.transfer.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import domain.SagaStatus;
import domain.TransferStep;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : com.baekgwa.transfer.domain
 * FileName    : TransferSagaRepository
 * Author      : Baekgwa
 * Date        : 25. 12. 8.
 * Description : 계좌 송금 처리(transfer)를 위한 전체 트랜잭션 상태 관리자
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 8.     Baekgwa               Initial creation
 */
@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class TransferSaga {

	@Id
	@Column(name = "saga_id", nullable = false)
	private String sagaId;

	@Column(nullable = false)
	private String fromAccountNumber;

	@Column(nullable = false)
	private String toAccountNumber;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SagaStatus status;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TransferStep step;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	public static TransferSaga newTransferSaga(String sagaId, String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
		return TransferSaga
			.builder()
			.sagaId(sagaId)
			.fromAccountNumber(fromAccountNumber)
			.toAccountNumber(toAccountNumber)
			.amount(amount)
			.status(SagaStatus.PENDING)
			.step(TransferStep.START)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	public void updateStatusStep(SagaStatus status, TransferStep step) {
		this.status = status;
		this.step = step;
	}
}
