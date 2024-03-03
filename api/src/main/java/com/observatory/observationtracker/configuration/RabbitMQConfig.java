package com.observatory.observationtracker.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.observation.notification}")
    private String observationNotificationQueue;

    @Value("${rabbitmq.exchange.observation.notification}")
    private String observationNotificationExchange;

    @Value("${rabbitmq.routing.key.observation.notification}")
    private String observationNotificationKey;


    @Bean
    public Queue jsonQueue() {
        return new Queue(observationNotificationQueue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(observationNotificationExchange);
    }


    @Bean
    public Binding jsonBinding() {
        return BindingBuilder.bind(jsonQueue())
                .to(exchange())
                .with(observationNotificationKey);
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
