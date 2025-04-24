package com.bdserver.impactassist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
@EnableAspectJAutoProxy
public class ImpactAssistApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImpactAssistApplication.class, args);
    }

}
