package com.dto.way.post.aws.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dto.way.post.aws.config.AmazonConfig;
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

    public String uploadFileToDirectory(String directoryPath, String keyName, MultipartFile file) {
        String fullKeyName = directoryPath + "/" + keyName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), fullKeyName, file.getInputStream(), metadata));
        } catch (IOException e) {
            log.error("Error at AmazonS3Manager uploadFileToDirectory: {}", (Object) e.getStackTrace());
        }
        return amazonS3.getUrl(amazonConfig.getBucket(), fullKeyName).toString();
    }


    public void deleteFile(String directoryPath, String fileUrl) throws IOException {
        int indexOfReviews = fileUrl.indexOf(directoryPath + "/");

        String fileKey = fileUrl.substring(indexOfReviews);
        try {
            amazonS3.deleteObject(amazonConfig.getBucket(), fileKey);
        } catch (SdkClientException e) {
            throw new IOException("Error deleting file from S3", e);
        }
    }

}