package com.observatory.observationtracker.aws;

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
import java.util.Date;
import java.util.Objects;

// @TODO Better error handling
@Service
public class S3Service {
    private final AwsConfig awsConfig;

    private final String region;
    private final String imageBucketName;

    public S3Service(AwsConfig awsConfig) {
        this.awsConfig = awsConfig;
        this.imageBucketName = awsConfig.getImageBucketName();
        this.region = awsConfig.getRegion();
    }

    public String uploadImage(MultipartFile image) throws InvalidImageException {
        S3Client s3Client = S3Client.builder()
                .region(Region.of(this.region))
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

        S3Client s3client = S3Client.builder()
                .region(Region.of(this.region))
                .build();

        s3client.deleteObject(DeleteObjectRequest.builder()
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
