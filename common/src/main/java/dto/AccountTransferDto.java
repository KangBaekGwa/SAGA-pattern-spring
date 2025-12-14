package dto;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * PackageName : dto
 * FileName    : AccountTransferDto
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountTransferDto {

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AccountWithdrawRequest {
		private String sagaId;
		private String accountNumber;
		private BigDecimal amount;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AccountDepositRequest {
		private String sagaId;
		private String accountNumber;
		private BigDecimal amount;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AccountWithdrawRollbackRequest {
		private String sagaId;
		private String accountNumber;
		private BigDecimal amount;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@ToString
	public static class AccountWithdrawResponse {
		private String sagaId;
		private String status;
		private String message;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@ToString
	public static class AccountDepositResponse {
		private String sagaId;
		private String status;
		private String message;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@ToString
	public static class AccountWithdrawRollbackResponse {
		private String sagaId;
		private String status;
		private String message;
	}
}
