package event;

import domain.SendStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : event
 * FileName    : TransferEvent
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferEvent {

	@Getter
	@NoArgsConstructor
	public static class TransferNotificationEvent extends SagaEvent {
		private String recipientAccountNumber;
		private SendStatus status;

		private TransferNotificationEvent(String sagaId, String message, String recipientAccountNumber,
			SendStatus status) {
			super(sagaId, message);
			this.recipientAccountNumber = recipientAccountNumber;
			this.status = status;
		}

		public static TransferNotificationEvent of(String sagaId, String recipientUserId, String message,
			SendStatus status) {
			return new TransferNotificationEvent(sagaId, message, recipientUserId, status);
		}
	}
}
