package com.observatory.observationtracker.rabbitmq.notifications;

import com.observatory.observationtracker.domain.observation.dto.GetObservationCommentDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommentNotificationProducer {
    @Value("${rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${rabbitmq.routing.key.observation.notification}")
    private String observationNotificationKey;

    private final RabbitTemplate rabbitTemplate;

    public CommentNotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void addObservationCommentMessage(GetUserAccountDto observationAuthor,
                                             GetObservationCommentDto createdComment) {
        CommentNotification notification = new CommentNotification(
                observationAuthor, createdComment);
        rabbitTemplate.convertAndSend(notificationExchange, observationNotificationKey, notification);
    }


}
