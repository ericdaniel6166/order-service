package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderStatusDto;
import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.integration.kafka.config.KafkaProducerProperties;
import com.example.orderservice.integration.kafka.event.OrderEvent;
import com.example.orderservice.integration.kafka.event.OrderPendingEvent;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatusHistory;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderStatusHistoryRepository;
import com.example.orderservice.service.OrderService;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.example.springbootmicroservicesframework.kafka.event.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
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

//    final InventoryClient inventoryClient;

    final KafkaProducerProperties kafkaProducerProperties;

    final ModelMapper modelMapper;

    final ObjectMapper objectMapper;

    final OrderMapper orderMapper;


    @Override
    public OrderStatusResponse getStatus(Long id) throws NotFoundException, JsonProcessingException {
        OrderStatusDto orderStatusDto = orderRepository.getStatus(id)
                .orElseThrow(() -> new NotFoundException(String.format("orderId %s", id)));

        JsonNode jsonNode = objectMapper.readTree(orderStatusDto.getOrderDetail());
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.remove(ORDER_ID);

        return orderMapper.mapToOrderStatusResponse(orderStatusDto, id,
                OrderStatusResponse.builder()
                        .orderDetail(objectNode)
                        .build());
    }

    @Transactional
    @Override
    public void handleOrderEvent(OrderEvent orderEvent, OrderStatus orderStatus) throws JsonProcessingException {
        String orderDetail = objectMapper.writeValueAsString(orderEvent);
        orderRepository.update(orderEvent.getOrderId(), orderStatus.name(),
                orderDetail, LocalDateTime.now());
        orderStatusHistoryRepository.saveAndFlush(OrderStatusHistory.builder()
                .orderId(orderEvent.getOrderId())
                .status(orderStatus.name())
                .orderDetail(orderDetail)
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
                .accountNumber(String.valueOf(RandomUtils.nextLong(100000000000L, 999999999999L))) //improvement later
                .orderId(order.getId())
                .orderPendingItemList(request.getOrderItemList().stream()
                        .map(orderItemDto -> modelMapper.map(orderItemDto, OrderPendingEvent.OrderPendingItem.class))
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

