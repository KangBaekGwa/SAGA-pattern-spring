package com.baekgwa.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baekgwa.account.domain.AccountHistory;

/**
 * PackageName : com.baekgwa.account.repository
 * FileName    : AccountHistoryRepository
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {
}
