package com.ttubeog.domain.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.S3ObjectResource;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class S3PhotoService {

    private final AmazonS3 amazonS3;

    public S3PhotoService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public InputStream downloadPhotoFromS3(String bucketName, String key) throws IOException {
        S3Object object = amazonS3.getObject(bucketName, key);

        return new ByteArrayInputStream(object.getObjectContent().readAllBytes());
    }
}
