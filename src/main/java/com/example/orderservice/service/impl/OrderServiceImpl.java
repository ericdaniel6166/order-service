package com.example.orderservice.service.impl;

import com.example.orderservice.config.kafka.KafkaProducerProperties;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderStatusDto;
import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.integration.client.InventoryClient;
import com.example.orderservice.integration.event.ItemNotAvailableEvent;
import com.example.orderservice.integration.event.OrderPendingEvent;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatusHistory;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderStatusHistoryRepository;
import com.example.orderservice.service.OrderService;
import com.example.springbootmicroservicesframework.dto.Event;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {

    static final String ORDER_ID = "orderId";

    final OrderRepository orderRepository;

    final OrderStatusHistoryRepository orderStatusHistoryRepository;

    final KafkaTemplate<String, Object> kafkaTemplate;

    final InventoryClient inventoryClient;

    final KafkaProducerProperties kafkaProducerProperties;

    final ModelMapper modelMapper;

    final ObjectMapper objectMapper;

    final OrderMapper orderMapper;


    @Override
    public OrderStatusResponse getStatus(Long id) throws NotFoundException, JsonProcessingException {
        OrderStatusDto orderStatusDto = orderRepository.getStatus(id)
                .orElseThrow(() -> new NotFoundException(String.format("orderId %s", id)));

        JsonNode jsonNode = objectMapper.readTree(orderStatusDto.getFailReason());
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.remove(ORDER_ID);

        return orderMapper.mapToOrderStatusResponse(orderStatusDto, id,
                OrderStatusResponse.builder()
                        .failReason(objectNode)
                        .build());
    }

    @Transactional
    @Override
    public void handleOrderItemNotAvailable(ItemNotAvailableEvent itemNotAvailableEvent) throws JsonProcessingException {
        String failReason = objectMapper.writeValueAsString(itemNotAvailableEvent);
        orderRepository.update(itemNotAvailableEvent.getOrderId(), OrderStatus.FAIL_ITEM_NOT_AVAILABLE.name(),
                failReason, LocalDateTime.now());
        orderStatusHistoryRepository.saveAndFlush(OrderStatusHistory.builder()
                .orderId(itemNotAvailableEvent.getOrderId())
                .status(OrderStatus.FAIL_ITEM_NOT_AVAILABLE.name())
                .failReason(failReason)
                .build());

    }

    @Transactional
    @Override
    public OrderStatusResponse place(OrderRequest request) {
        Order order = Order.builder()
                .status(OrderStatus.PENDING.name())
                .userId(request.getUserId())
                .build();
        orderRepository.saveAndFlush(order);
        orderStatusHistoryRepository.saveAndFlush(OrderStatusHistory.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .build());

        OrderPendingEvent orderPendingEvent = OrderPendingEvent.builder()
                .orderId(order.getId())
                .orderItemList(request.getOrderItemList().stream()
                        .map(orderItemDto -> modelMapper.map(orderItemDto, OrderPendingEvent.OrderItemDto.class))
                        .toList())
                .build();
        log.info("send orderPendingEvent {}", orderPendingEvent);
        kafkaTemplate.send(kafkaProducerProperties.getOrderPendingTopicName(), Event.builder()
                .payload(orderPendingEvent)
                .build());
        return OrderStatusResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .build();
    }

}

