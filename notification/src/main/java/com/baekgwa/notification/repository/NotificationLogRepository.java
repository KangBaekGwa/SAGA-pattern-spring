package com.baekgwa.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baekgwa.notification.domain.NotificationLog;

/**
 * PackageName : com.baekgwa.notification.repository
 * FileName    : NotificationLogRepository
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}
