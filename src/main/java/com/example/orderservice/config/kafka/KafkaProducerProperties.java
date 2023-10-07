package com.example.orderservice.config.kafka;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class KafkaProducerProperties {

    @Value("${spring.kafka.producers.internal-producer.topic-name}")
    String internalTopicName;

    @Value("${spring.kafka.producers.notification-producer.topic-name}")
    String notificationTopicName;


}
