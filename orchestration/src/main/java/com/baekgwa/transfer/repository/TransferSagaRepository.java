package com.baekgwa.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baekgwa.transfer.domain.TransferSaga;

/**
 * PackageName : com.baekgwa.transfer.repository
 * FileName    : TransferSagaRepository
 * Author      : Baekgwa
 * Date        : 25. 12. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 8.     Baekgwa               Initial creation
 */
public interface TransferSagaRepository extends JpaRepository<TransferSaga, String> {
}
