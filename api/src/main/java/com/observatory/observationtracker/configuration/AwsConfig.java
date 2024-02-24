package com.observatory.observationtracker.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${region}")
    private String region;

    @Value("${s3.bucket.name}")
    private String imageBucketName;

    public String getRegion() {
        return region;
    }

    public String getImageBucketName() {
        return imageBucketName;
    }
}
