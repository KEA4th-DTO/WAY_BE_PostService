package com.dto.way.post.service.notificationService;

import com.dto.way.message.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j(topic = "like")
@RequiredArgsConstructor
public class NotificationService {
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;
    @Value("${spring.kafka.template.default-topic}")
    private String topicName;

    public void postNotificationCreate(NotificationMessage notificationMessage) {
        Message<NotificationMessage> notification = MessageBuilder
                .withPayload(notificationMessage)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();
        CompletableFuture<SendResult<String, NotificationMessage>> future =
                kafkaTemplate.send(notification);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("producer: success >>> message: {}, offset: {}",
                        result.getProducerRecord().value().toString(), result.getRecordMetadata().offset());
            } else {
                log.info("producer: failure >>> message: {}", ex.getMessage());
            }
        });
    }

    public NotificationMessage createNotificationMessage(Long targetMemberId, String targetMemberNickname, String message) {
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setTargetMemberId(targetMemberId);
        notificationMessage.setTargetMemberNickname(targetMemberNickname);
        notificationMessage.setMessage(message);
        notificationMessage.setCreatedAt(LocalDateTime.now());
        return notificationMessage;
    }
}