package com.dto.way.post.aws.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dto.way.post.domain.common.Uuid;
import com.dto.way.post.global.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager{

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
        }catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    public void deleteFile(String fileUrl) throws IOException{
        int indexOfReviews = fileUrl.indexOf("daily_image/");
        String fileKey = fileUrl.substring(indexOfReviews);
        try{
            amazonS3.deleteObject(amazonConfig.getBucket(),fileKey);
        }catch (SdkClientException e) {
            throw new IOException("Error deleting file from S3", e);
        }
    }

    public String generateReviewKeyName(Uuid uuid) {
        return amazonConfig.getDailyImagePath() + '/' + uuid.getUuid();
    }

}