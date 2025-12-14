package domain;

/**
 * PackageName : domain
 * FileName    : TransactionType
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
public enum TransactionType {
	DEBIT, //출금
	CREDIT, //입금
	ROLLBACK_DEBIT, //출금 취소
	ROLLBACK_CREDIT //입금 취소
}
