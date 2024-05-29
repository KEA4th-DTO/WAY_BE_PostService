package com.dto.way.post.aws.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Service
public class S3FileService {

    private final S3Client s3Client;
    private final String bucketName;
    private final int maxQueueSize;

    private static final String BLOCK_DELIMITER = "###-------###";

    public S3FileService(@Value("${cloud.aws.credentials.accessKey}") String accessKeyId,
                         @Value("${cloud.aws.credentials.secretKey}") String secretAccessKey,
                         @Value("${cloud.aws.region.static}") String region,
                         @Value("${cloud.aws.s3.bucket}") String bucketName) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        this.bucketName = bucketName;
        this.maxQueueSize = 10;
    }

    @Async
    public CompletableFuture<String> saveOrUpdateFileAsync(String content, String key) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 기존 파일 다운로드
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                Queue<String> contentQueue = new LinkedList<>();
                try {
                    InputStream inputStream = s3Client.getObject(getObjectRequest);
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        StringBuilder currentBlock = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.equals(BLOCK_DELIMITER)) {
                                contentQueue.add(currentBlock.toString().trim());
                                currentBlock.setLength(0);
                            } else {
                                currentBlock.append(line).append("\n");
                            }
                        }
                        // 마지막 블록 추가
                        if (currentBlock.length() > 0) {
                            contentQueue.add(currentBlock.toString().trim());
                        }
                    }
                } catch (S3Exception e) {
                    // 파일이 존재하지 않을 경우 새로 생성
                    if (e.statusCode() != 404) {
                        throw e; // 다른 오류는 다시 던짐
                    }
                }

                // 새로운 내용을 큐의 뒤에 추가
                contentQueue.add(content.trim());

                // 큐의 크기가 maxQueueSize를 초과하면, 가장 오래된 요소를 제거
                while (contentQueue.size() > maxQueueSize) {
                    contentQueue.poll();
                }

                // 큐의 내용을 StringBuilder에 저장
                StringBuilder fileContent = new StringBuilder();
                for (String block : contentQueue) {
                    fileContent.append(block).append("\n").append(BLOCK_DELIMITER).append("\n");
                }

                // 수정된 파일을 다시 업로드
                InputStream updatedInputStream = new ByteArrayInputStream(fileContent.toString().getBytes(StandardCharsets.UTF_8));
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .contentType("text/plain; charset=utf-8")
                        .key(key)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(updatedInputStream, fileContent.length()));

                // 파일의 URL 반환
                return s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(key)).toString();

            } catch (S3Exception | IOException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException("Failed to save or update file in S3", e);
            }
        });
    }
}
