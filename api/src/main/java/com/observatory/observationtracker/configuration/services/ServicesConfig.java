package com.observatory.observationtracker.configuration.services;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;

/**
 * Provides a contract to configure and use AWS's client classes
 * with either Localstack's mock S3 bucket or the bucket hosted on AWS
 * according to the current active profile.
 */
public interface ServicesConfig {
    public String getRegion();

    public S3Client s3Client();

    public String getImageBucketName();

    public String getNotificationSenderEmail();

    public SesClient sesClient();
}
