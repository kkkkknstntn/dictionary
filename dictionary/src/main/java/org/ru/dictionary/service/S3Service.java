package org.ru.dictionary.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${s3.url}")
    private String minioUrl;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.info("Uploading file {} to bucket {}", file.getOriginalFilename(), bucketName);
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
        log.info("Uploading file {} to bucket {}", file.getOriginalFilename(), bucketName);
        return (minioUrl + "/" + bucketName + "/" + key);
    }
}