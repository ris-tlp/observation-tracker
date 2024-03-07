package com.observatory.observationtracker.aws;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import com.observatory.observationtracker.aws.exceptions.InvalidImageException;
import com.observatory.observationtracker.configuration.AwsConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Objects;

// @TODO Better error handling
@Service
public class S3Service {
    private final AwsConfig awsConfig;

    private String region;
    private String imageBucketName;

    public S3Service(AwsConfig awsConfig) {
        this.awsConfig = awsConfig;
        this.imageBucketName = awsConfig.getImageBucketName();
        this.region = awsConfig.getRegion();
    }

    public String uploadImage(MultipartFile image) throws InvalidImageException {
//        URI uri = URI.create(
//                String.format("https://s3.%s.amazonaws.com/", region));
        URI uri = URI.create(
                "http://localhost:4566/"
        );

        region = "us-east-1";

        imageBucketName = "bucketname";

        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(uri)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        try {
            File convertedImage = convertMultipartFileToFile(image);
            String uploadedFileName = generateFilename(image.getOriginalFilename());
            PutObjectRequest putObjectRequest =
                    PutObjectRequest.builder()
                            .bucket(imageBucketName)
                            .key(uploadedFileName)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build();


            s3Client.putObject(putObjectRequest, convertedImage.toPath());

            String url = "https://" + imageBucketName + ".s3." + region + ".amazonaws.com/" + uploadedFileName;
            convertedImage.delete();

            return url;
        } catch (IOException e) {
            throw new InvalidImageException();
        }
    }

    // @TODO Better error handling
    public void deleteImage(String imageUrl) {
        String key = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

        URI uri = URI.create(
                String.format("https://s3.%s.amazonaws.com/", region));

        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(uri)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(imageBucketName)
                .key(key)
                .build());
    }

    private File convertMultipartFileToFile(MultipartFile image) throws IOException {
        File convertedImage = new File(Objects.requireNonNull(image.getOriginalFilename()));
        FileOutputStream stream = new FileOutputStream(convertedImage);
        stream.write(image.getBytes());
        stream.close();

        return convertedImage;
    }

    private String generateFilename(String originalName) {
        return String.format(
                "%s-%s",
                new Date().getTime(),
                originalName.replace(" ", "_")
        );
    }
}
