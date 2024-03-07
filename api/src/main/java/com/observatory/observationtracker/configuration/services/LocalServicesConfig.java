package com.observatory.observationtracker.configuration.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;

import java.net.URI;

/**
 * All values here are injected through application-dev.properties.
 * Values in application-dev are loaded in through the .env file which
 * is passed in through docker compose to the api service.
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
        return S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(getLocalstackUri())
                .build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .endpointOverride(getLocalstackUri())
                .region(Region.of(region))
                .build();
    }

    public String getImageBucketName() {
        return bucketName;
    }

    public String getNotificationSenderEmail() {
        return sesEmail;
    }

    private URI getLocalstackUri() {
        return URI.create(String.format("http://%s:%s/", localstackHost, localstackPort));
    }

}
