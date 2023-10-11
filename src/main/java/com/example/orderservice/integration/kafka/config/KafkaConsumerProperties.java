package com.example.orderservice.integration.kafka.config;

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
public class KafkaConsumerProperties {

    @Value("${spring.kafka.consumers.order-item-not-available.group-id}")
    String orderItemNotAvailableGroupId;

    @Value("${spring.kafka.consumers.order-processing.group-id}")
    String orderProcessingGroupId;

    @Value("${spring.kafka.consumers.order-payment-processing.group-id}")
    String orderPaymentProcessingGroupId;


}
