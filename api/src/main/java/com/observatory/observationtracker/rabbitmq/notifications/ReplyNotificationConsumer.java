package com.observatory.observationtracker.rabbitmq.notifications;

import com.observatory.observationtracker.aws.SesService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReplyNotificationConsumer {
    private final SesService sesService;

    public ReplyNotificationConsumer(SesService sesService) {
        this.sesService = sesService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.reply.notification}"})
    public void consumeJsonMessage(ReplyNotification notification) {
        prepareAndSendEmail(notification);
    }

    public void prepareAndSendEmail(ReplyNotification notification) {
        String body = String.format("Hi %s! Your comment has received a new reply: %s by %s",
                notification.commentAuthor().getName(),
                notification.createdReply().getContent(),
                notification.replyAuthor().getName()
                );

        String subject = String.format("Hi %s! You've received a new comment!",
                notification.commentAuthor().getName());

        String toEmail = notification.commentAuthor().getEmail();

        sesService.sendEmail(subject, body, toEmail);
    }
}
