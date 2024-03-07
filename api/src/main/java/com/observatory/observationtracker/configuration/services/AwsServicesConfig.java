package com.observatory.observationtracker.configuration.services;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;

import javax.sql.DataSource;
import java.net.URI;

/**
 * All values in this configuration are injected directly from
 * AWS's System Parameter Store. SSM parameters are automatically initialized
 * once the services are created on AWS through Terraform.
 */
@Configuration
@Profile("prod")
public class AwsServicesConfig implements ServicesConfig {
    @Value("${region}")
    private String region;

    // S3 Parameters
    @Value("${s3.bucket.name}")
    private String imageBucketName;

    // RDS Parameters
    @Value("${rds.username}")
    private String rdsUsername;

    @Value("${rds.password}")
    private String rdsPassword;

    @Value("${rds.port}")
    private String rdsPort;

    @Value("${rds.database}")
    private String rdsName;

    @Value("${rds.endpoint}")
    private String rdsUri;

    // SES Parameters
    @Value("${notification.sender-email}")
    private String notificationSenderEmail;

    // RabbitMQ Parameters
    @Value("${rabbitmq.username}")
    private String rabbitMqUsername;

    @Value("${rabbitmq.password}")
    private String rabbitMqPassword;

    @Value("${rabbitmq.endpoint}")
    private String rabbitMqEndpoint;

    // ElastiCache Parameters
    @Value("${elasticache.endpoint}")
    private String elasticacheEndpoint;

    @Value("${elasticache.port}")
    private String elasticachePort;

    @Bean
    public S3Client s3Client() {
        URI uri = URI.create(
                String.format("https://s3.%s.amazonaws.com/", region));

        return S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(uri)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder().build();
    }

    @Bean
    public DataSource getDataSource() {
        String driverClassName = org.postgresql.Driver.class.getName();
        String formattedRdsUri = "jdbc:postgresql://" + rdsUri + "/" + rdsName;

        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(formattedRdsUri);
        builder.username(rdsUsername);
        builder.password(rdsPassword);
        builder.driverClassName(driverClassName);

        return builder.build();
    }

    @Bean
    RabbitConnectionFactoryBean rabbitConnectionFactory() {
        return new RabbitConnectionFactoryBean();
    }

    @Bean
    CachingConnectionFactory connectionFactory(ConnectionFactory rabbitConnectionFactory) {
        CachingConnectionFactory factory = new CachingConnectionFactory(rabbitConnectionFactory);
        factory.setUri(rabbitMqEndpoint);
        factory.setUsername(rabbitMqUsername);
        factory.setPassword(rabbitMqPassword);
        return factory;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(elasticacheEndpoint,
                Integer.parseInt(elasticachePort));
        return new LettuceConnectionFactory(configuration);
    }

    public String getRegion() {
        return region;
    }

    public String getImageBucketName() {
        return imageBucketName;
    }

    public String getRdsUsername() {
        return rdsUsername;
    }

    public String getRdsPassword() {
        return rdsPassword;
    }

    public String getRdsPort() {
        return rdsPort;
    }

    public String getRdsName() {
        return rdsName;
    }

    public String getRdsUri() {
        return rdsUri;
    }

    public String getNotificationSenderEmail() {
        return notificationSenderEmail;
    }

    public String getRabbitMqUsername() {
        return rabbitMqUsername;
    }

    public String getRabbitMqPassword() {
        return rabbitMqPassword;
    }

    public String getRabbitMqEndpoint() {
        return rabbitMqEndpoint;
    }

    public String getElasticacheEndpoint() {
        return elasticacheEndpoint;
    }

    public String getElasticachePort() {
        return elasticachePort;
    }
}
