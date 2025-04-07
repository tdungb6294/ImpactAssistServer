package com.bdserver.impactassist.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @KafkaListener(topics = "my-topic", groupId = "hello-world")
    public void listen(String message) {
        System.out.println(message);
    }
}
