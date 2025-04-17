package com.bdserver.impactassist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {
    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create("minio", "minio123");
        return S3Client.builder()
                .endpointOverride(URI.create("http://localhost:9000"))
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    @DependsOn("s3Client")
    public S3Presigner s3Presigner() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create("minio", "minio123");
        return S3Presigner.builder()
                .endpointOverride(URI.create("http://192.168.1.92:9000"))
                .s3Client(s3Client())
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.EU_NORTH_1)
                .build();
    }

}
