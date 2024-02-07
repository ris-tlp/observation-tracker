package com.observatory.observationscheduler.awsservice;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.observatory.observationscheduler.awsservice.exceptions.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class S3Service {
    @Value("${region}")
    private String region;

    @Value("${s3.bucket.name}")
    private String bucketName;

    // @TODO images are being locally saved, make sure to delete them
    public String uploadImage(MultipartFile image)  {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(this.region).build();
        try {
            PutObjectRequest request = new PutObjectRequest(
                    this.bucketName, image.getOriginalFilename(), convertMultipartFileToFile(image)
            );
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            String url = String.valueOf(s3Client.getUrl(bucketName, image.getOriginalFilename()));

            s3Client.putObject(request);

            return url;

        } catch (IOException e) {
            return null;
        }
    }

    private File convertMultipartFileToFile(MultipartFile image) throws IOException {
        File convertedImage = new File(Objects.requireNonNull(image.getOriginalFilename()));
        FileOutputStream stream = new FileOutputStream(convertedImage);
        stream.write(image.getBytes());
        stream.close();

        return convertedImage;
    }
}
