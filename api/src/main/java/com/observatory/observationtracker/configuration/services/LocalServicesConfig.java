package com.observatory.observationtracker.configuration.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;

import java.net.URI;

/**
 * All values here are injected through the application-dev.properties file.
 * Values in this file are initialized through the .env file in the root folder
 * which is mounted on through docker-compose in the api service.
 */

@Configuration
@Profile("dev")
public class LocalServicesConfig implements ServicesConfig {
    @Value("${localstack.region}")
    private String region;

    @Value("${localstack.s3.bucket.name}")
    private String bucketName;

    @Value("${localstack.ses.email}")
    private String sesEmail;

    @Value("${localstack.host}")
    private String localstackHost;

    @Value("${localstack.port}")
    private String localstackPort;

    public String getRegion() {
        return region;
    }

    @Bean
    public S3Client s3Client() {
        URI uri = URI.create(String.format("http://%s:%s/", localstackHost, localstackPort));
        System.out.println(uri);
        return S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(uri)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.of(region))
                .build();
    }

    public String getImageBucketName() {
        return bucketName;
    }

    public String getNotificationSenderEmail() {
        return sesEmail;
    }

}
