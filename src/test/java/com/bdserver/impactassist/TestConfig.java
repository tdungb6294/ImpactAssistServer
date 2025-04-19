package com.bdserver.impactassist;

import com.bdserver.impactassist.service.S3Service;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {
    @Bean
    public S3Service s3Service() {
        return mock(S3Service.class);
    }
}
