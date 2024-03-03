package com.observatory.observationtracker.rabbitmq.notifications;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    @RabbitListener(queues = {"${rabbitmq.queue.observation.notification}"})
    public void consumeJsonMessage(ObservationCommentNotification notification){
        System.out.println((String.format("Received json message -> %s", notification.toString())));
    }
}
