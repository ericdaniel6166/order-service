package com.example.orderservice.integration.kafka.config;

import com.example.springbootmicroservicesframework.integration.kafka.config.KafkaConsumerConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerGroupIdConfig {

    KafkaConsumerConfig kafkaConsumerConfig;

    KafkaConsumerProperties kafkaConsumerProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> orderItemNotAvailableKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory(kafkaConsumerProperties.getOrderItemNotAvailableGroupId());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> orderProcessingKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory(kafkaConsumerProperties.getOrderProcessingGroupId());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> orderPaymentProcessingKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory(kafkaConsumerProperties.getOrderPaymentProcessingGroupId());
    }


}
