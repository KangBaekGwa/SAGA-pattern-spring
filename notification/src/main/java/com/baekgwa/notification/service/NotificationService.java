package com.baekgwa.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import event.TopicList;
import event.TransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PackageName : com.baekgwa.notification.service
 * FileName    : NotificationService
 * Author      : Baekgwa
 * Date        : 25. 12. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 12. 9.     Baekgwa               Initial creation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	@KafkaListener(
		topics = TopicList.TRANSFER_NOTIFICATION_TOPIC,
		groupId = "notification-service-group"
	)
	public void handleNotification(
		@Payload TransferEvent.TransferNotificationEvent event,
		@Header(KafkaHeaders.RECEIVED_KEY) String sagaId
	) {
		log.info("========== [알림 수신] ==========");
		log.info("Saga ID    : {}", sagaId);
		log.info("수신자 계좌 : {}", event.getRecipientAccountNumber());
		log.info("메시지      : {}", event.getMessage());
		log.info("전송 상태   : {}", event.getStatus());

		try {
			// todo: 실제 알림 발송 로직 추가.

			log.info("✅ 알림 발송 성공");
		} catch (Exception e) {
			log.error("❌ 알림 발송 실패: {}", e.getMessage());
			// 필요한 경우 여기서 재시도(Retry) 토픽으로 보내거나 DLQ 처리
		}
	}
}
