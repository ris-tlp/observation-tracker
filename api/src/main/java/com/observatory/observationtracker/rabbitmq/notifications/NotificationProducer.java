package com.observatory.observationtracker.rabbitmq.notifications;

import com.observatory.observationtracker.domain.observation.dto.GetObservationCommentDto;
import com.observatory.observationtracker.domain.useraccount.dto.GetUserAccountDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    @Value("${rabbitmq.exchange.observation.notification}")
    private String observationNotificationExchange;

    @Value("${rabbitmq.routing.key.observation.notification}")
    private String observationNotificationKey;

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendObservationCommentMessage(GetUserAccountDto observationAuthor,
                                              GetObservationCommentDto createdComment) {
        ObservationCommentNotification notification = new ObservationCommentNotification(
                observationAuthor, createdComment);
        rabbitTemplate.convertAndSend(observationNotificationExchange, observationNotificationKey, notification);
    }
}
