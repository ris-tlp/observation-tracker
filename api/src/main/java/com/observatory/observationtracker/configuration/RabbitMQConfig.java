package com.observatory.observationtracker.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${rabbitmq.queue.observation.notification}")
    private String commentNotificationQueue;

    @Value("${rabbitmq.routing.key.observation.notification}")
    private String commentNotificationKey;

    @Value("${rabbitmq.queue.reply.notification}")
    private String replyNotificationQueue;

    @Value("${rabbitmq.routing.key.reply.notification}")
    private String replyNotificationKey;


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(notificationExchange);
    }

    @Bean
    public Queue commentNotificationQueue() {
        return new Queue(commentNotificationQueue);
    }

    @Bean
    public Binding commentNotificationBinding() {
        return BindingBuilder.bind(commentNotificationQueue())
                .to(exchange())
                .with(commentNotificationKey);
    }

    @Bean
    public Queue replyNotificationQueue() { return new Queue(replyNotificationQueue);}

    @Bean
    public Binding replyNotificationBinding() {
        return BindingBuilder.bind(replyNotificationQueue())
                .to(exchange())
                .with(replyNotificationKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
