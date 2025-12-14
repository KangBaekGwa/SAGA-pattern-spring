package event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : event
 * FileName    : SagaEvent
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class SagaEvent {
	private String sagaId;
	private String message;
}
