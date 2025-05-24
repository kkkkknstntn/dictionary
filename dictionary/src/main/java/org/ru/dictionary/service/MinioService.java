package org.ru.dictionary.service;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioService(MinioClient minioClient, @Value("${s3.bucket}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @PostConstruct
    public void init() {
        try {
            // Проверка существования бакета
            BucketExistsArgs existsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            if (!minioClient.bucketExists(existsArgs)) {
                // Создание бакета, если его нет
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
                System.out.println("Bucket created: " + bucketName);
            } else {
                System.out.println("Bucket already exists: " + bucketName);
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error connecting to MinIO: " + e.getMessage());
        }
    }
}
