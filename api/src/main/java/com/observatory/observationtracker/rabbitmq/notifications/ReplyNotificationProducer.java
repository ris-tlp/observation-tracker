package com.observatory.observationtracker.rabbitmq.notifications;

import com.observatory.observationtracker.domain.observation.dto.GetObservationCommentDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import com.observatory.observationtracker.rabbitmq.notifications.models.ReplyNotification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReplyNotificationProducer {
    @Value("${rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${rabbitmq.routing.key.reply.notification}")
    private String replyNotificationKey;

    private final RabbitTemplate rabbitTemplate;

    public ReplyNotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void addReplyMessage(GetUserAccountDto commentAuthor,
                                GetUserAccountDto replyAuthor,
                                GetObservationCommentDto createdReply) {
        ReplyNotification notification = new ReplyNotification(commentAuthor, replyAuthor, createdReply);
        rabbitTemplate.convertAndSend(notificationExchange, replyNotificationKey, notification);
    }
}
