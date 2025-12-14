package domain;

/**
 * PackageName : domain
 * FileName    : TransferStep
 * Author      : Baekgwa
 * Date        : 25. 12. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 8.     Baekgwa               Initial creation
 */
public enum TransferStep {
	START,

	WITHDRAW_START, // 출금 시작
	WITHDRAW_END, // 출금 완료
	DEPOSIT_START, // 입금 시작
	DEPOSIT_END, // 입금 완료

	LEDGER, // 원장 서비스 기록
	FINISH, // 통합 완료
	COMPENSATE // 보상 처리
}
