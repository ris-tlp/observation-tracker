package com.observatory.observationtracker.configuration;

import com.observatory.observationtracker.configuration.AwsConfig;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import(AwsConfig.class)
@Profile("prod")
public class AmqpConnectionConfig {
    private final String amqpAddress;
    private final String amqpUsername;
    private final String amqpPassword;

    public AmqpConnectionConfig(AwsConfig awsConfig) {
        this.amqpUsername = awsConfig.getRabbitMqUsername();
        this.amqpPassword = awsConfig.getRabbitMqPassword();
        this.amqpAddress = awsConfig.getRabbitMqEndpoint();
    }

    @Bean
    RabbitConnectionFactoryBean rabbitConnectionFactory() {
        return new RabbitConnectionFactoryBean();
    }

    @Bean
    CachingConnectionFactory connectionFactory(ConnectionFactory rabbitConnectionFactory) {
        CachingConnectionFactory factory = new CachingConnectionFactory(rabbitConnectionFactory);
        factory.setUri(amqpAddress);
        factory.setUsername(amqpUsername);
        factory.setPassword(amqpPassword);
        return factory;
    }
}