package com.observatory.observationtracker.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
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
}
