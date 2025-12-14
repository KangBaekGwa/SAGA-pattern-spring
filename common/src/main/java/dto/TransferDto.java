package dto;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : dto
 * FileName    : TransferDto
 * Author      : Baekgwa
 * Date        : 25. 12. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 8.     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TransferRequest {
		private String fromAccountNumber;
		private String toAccountNumber;
		private BigDecimal amount;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class TransferResponse {
		private final String sagaId;
		private final String status;
		private final String message;

		public static TransferResponse of(String sagaId, String status, String message) {
			return new TransferResponse(sagaId, status, message);
		}
	}
}
