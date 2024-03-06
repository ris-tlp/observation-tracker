package com.observatory.observationtracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Import(AwsConfig.class)
@Profile("prod")
public class RedisConnectionConfig {
    private final String elasticacheEndpoint;
    private final String elasticachePort;

    public RedisConnectionConfig(AwsConfig awsConfig) {
        this.elasticacheEndpoint = awsConfig.getElasticacheEndpoint();
        this.elasticachePort = awsConfig.getElasticachePort();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
                elasticacheEndpoint, Integer.parseInt(elasticachePort)
        );
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
