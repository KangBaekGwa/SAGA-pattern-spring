package com.baekgwa.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.baekgwa.account.domain.Account;

import jakarta.persistence.LockModeType;

/**
 * PackageName : com.baekgwa.account.repository
 * FileName    : AccountRepository
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from Account a where a.accountNumber = :accountNumber")
	Optional<Account> findByAccountNumberWithLock(String accountNumber);
}
