package com.observatory.observationtracker.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.observatory.observationtracker.aws.exceptions.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
    @Value("${region}")
    private String region;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public String uploadImage(MultipartFile image) throws InvalidImageException {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(this.region).build();
        try {
            File convertedImage = convertMultipartFileToFile(image);
            String uploadedFileName = generateFilename(image.getOriginalFilename());
            PutObjectRequest request = new PutObjectRequest(
                    this.bucketName, uploadedFileName, convertedImage
            );
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            String url = String.valueOf(s3Client.getUrl(bucketName, uploadedFileName));
            s3Client.putObject(request);
            convertedImage.delete();

            return url;
        } catch (IOException e) {
            throw new InvalidImageException();
        }
    }

    // @TODO Better error handling
    public void deleteImage(String imageUrl) {
        AmazonS3URI uri = new AmazonS3URI(imageUrl);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(this.region).build();
        s3client.deleteObject(this.bucketName, uri.getKey());
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
