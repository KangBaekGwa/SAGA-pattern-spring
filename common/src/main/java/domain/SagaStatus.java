package domain;

/**
 * PackageName : domain
 * FileName    : SagaStatus
 * Author      : Baekgwa
 * Date        : 25. 12. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 8.     Baekgwa               Initial creation
 */
public enum SagaStatus {
	STARTED, //시작
	PENDING, //대기
	COMPLETED, //완료
	ABORTED, // 명시적 취소, 성공된 실패 (복구 성공)
	FAILED // 실패
}
