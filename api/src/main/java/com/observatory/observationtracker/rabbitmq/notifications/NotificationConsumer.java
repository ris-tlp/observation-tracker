package com.observatory.observationtracker.rabbitmq.notifications;

import com.observatory.observationtracker.aws.SesService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class NotificationConsumer {
    private final SesService sesService;

    public NotificationConsumer(SesService sesService) {
        this.sesService = sesService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.observation.notification}"})
    public void consumeJsonMessage(ObservationCommentNotification notification) {
        prepareAndSendEmail(notification);
    }


    public void prepareAndSendEmail(ObservationCommentNotification notification) {
        String body = String.format("Hi %s! Your observation has received a new comment: %s by %s",
                notification.observationAuthor().getName(), notification.createdComment().getContent(),
                notification.createdComment().getAuthor().getName());

        String subject = String.format("Hi %s! You've received a new comment!",
                notification.observationAuthor().getName());

        String toEmail = notification.observationAuthor().getEmail();

        sesService.sendEmail(subject, body, toEmail);
    }


}
