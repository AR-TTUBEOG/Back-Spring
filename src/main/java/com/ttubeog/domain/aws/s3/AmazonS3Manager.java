package com.ttubeog.domain.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ttubeog.domain.UuidImage.domain.UuidImage;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.global.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
        }catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    public String generateSpotKeyName(Image image) {
        return amazonConfig.getSpotPath() + '/' + image.getUuid();
    }

    public String generateStoreKeyName(Image image) {
        return amazonConfig.getStorePath() + '/' + image.getUuid();
    }

    public String generateGuestBookKeyName(Image image) {
        return amazonConfig.getGuestBookPath() + '/' + image.getUuid();
    }
}
