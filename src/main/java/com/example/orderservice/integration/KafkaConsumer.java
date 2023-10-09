package com.example.orderservice.integration;

import com.example.orderservice.integration.event.ItemNotAvailableEvent;
import com.example.orderservice.service.OrderService;
import com.example.springbootmicroservicesframework.dto.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class KafkaConsumer {

    final ModelMapper modelMapper;
    final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.consumers.order-item-not-available.topic-name}",
            groupId = "${spring.kafka.consumers.order-item-not-available.group-id}",
            containerFactory = "orderItemNotAvailableKafkaListenerContainerFactory",
            concurrency = "${spring.kafka.consumers.order-item-not-available.properties.concurrency}"
    )
    public void handleOrderItemNotAvailable(Event kafkaEvent) throws JsonProcessingException {
        var itemNotAvailableEvent = modelMapper.map(kafkaEvent.getPayload(), ItemNotAvailableEvent.class);
        log.info("handle itemNotAvailableEvent {}", itemNotAvailableEvent);
        orderService.handleOrderItemNotAvailable(itemNotAvailableEvent);
    }

}
