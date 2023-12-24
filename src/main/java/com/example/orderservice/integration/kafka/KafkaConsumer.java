package com.example.orderservice.integration.kafka;

import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.integration.kafka.event.ItemNotAvailableEvent;
import com.example.orderservice.integration.kafka.event.OrderPaymentProcessingEvent;
import com.example.orderservice.integration.kafka.event.OrderProcessingEvent;
import com.example.orderservice.service.OrderService;
import com.example.springbootmicroservicesframework.integration.kafka.event.Event;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KafkaConsumer {

    ModelMapper modelMapper;
    OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.consumers.order-item-not-available.topic-name}",
            groupId = "${spring.kafka.consumers.order-item-not-available.group-id}",
            containerFactory = "orderItemNotAvailableKafkaListenerContainerFactory",
            concurrency = "${spring.kafka.consumers.order-item-not-available.properties.concurrency}"
    )
    public void handleOrderItemNotAvailable(Event kafkaEvent) throws JsonProcessingException {
        var itemNotAvailableEvent = modelMapper.map(kafkaEvent.getPayload(), ItemNotAvailableEvent.class);
        log.info("handle itemNotAvailableEvent {}", itemNotAvailableEvent);
        orderService.handleOrderEvent(itemNotAvailableEvent, OrderStatus.ITEM_NOT_AVAILABLE);
    }

    @KafkaListener(topics = "${spring.kafka.consumers.order-processing.topic-name}",
            groupId = "${spring.kafka.consumers.order-processing.group-id}",
            containerFactory = "orderProcessingKafkaListenerContainerFactory",
            concurrency = "${spring.kafka.consumers.order-processing.properties.concurrency}"
    )
    public void handleOrderProcessing(Event kafkaEvent) throws JsonProcessingException {
        var orderProcessingEvent = modelMapper.map(kafkaEvent.getPayload(), OrderProcessingEvent.class);
        log.info("handle orderProcessingEvent {}", orderProcessingEvent);
        orderService.handleOrderEvent(orderProcessingEvent, OrderStatus.PROCESSING);
    }

    @KafkaListener(topics = "${spring.kafka.consumers.order-payment-processing.topic-name}",
            groupId = "${spring.kafka.consumers.order-payment-processing.group-id}",
            containerFactory = "orderPaymentProcessingKafkaListenerContainerFactory",
            concurrency = "${spring.kafka.consumers.order-payment-processing.properties.concurrency}"
    )
    public void handleOrderPaymentProcessing(Event kafkaEvent) throws JsonProcessingException {
        var orderPaymentProcessingEvent = modelMapper.map(kafkaEvent.getPayload(), OrderPaymentProcessingEvent.class);
        log.info("handle orderPaymentProcessingEvent {}", orderPaymentProcessingEvent);
        orderService.handleOrderEvent(orderPaymentProcessingEvent, OrderStatus.PAYMENT_PROCESSING);
    }

}
