package org.ru.dictionary.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {

    @Value("${s3.url}")
    private String minioUrl;

    @Value("${s3.accessKeyId}")
    private String accessKey;

    @Value("${s3.secretAccessKey}")
    private String secretKey;

    @Value("${s3.bucket}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
}
