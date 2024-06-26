package com.dto.way.post.service.notificationService;

import com.dto.way.message.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    @Async
    public CompletableFuture<Void> sendNotification(String topic, NotificationMessage notificationMessage) {
        Message<NotificationMessage> notification = MessageBuilder
                .withPayload(notificationMessage)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        CompletableFuture<SendResult<String, NotificationMessage>> future =
                kafkaTemplate.send(notification);
        return future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("producer: success >>> message: {}, offset: {}",
                        result.getProducerRecord().value().toString(), result.getRecordMetadata().offset());
            } else {
                log.info("producer: failure >>> message: {}", ex.getMessage());
            }
        }).thenApply(result -> null);
    }

    @Async
    public CompletableFuture<Void> likeNotificationCreate(NotificationMessage notificationMessage) {
        return sendNotification("like", notificationMessage);
    }

    @Async
    public CompletableFuture<Void> commentNotificationCreate(NotificationMessage notificationMessage) {
        return sendNotification("comment", notificationMessage);
    }

    @Async
    public CompletableFuture<Void> replyNotificationCreate(NotificationMessage notificationMessage) {
        return sendNotification("reply", notificationMessage);
    }

    @Async
    public CompletableFuture<Void> commentLikeNotificationCreate(NotificationMessage notificationMessage) {
        return sendNotification("commentLike", notificationMessage);
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
