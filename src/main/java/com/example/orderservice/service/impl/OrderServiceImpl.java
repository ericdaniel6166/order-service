package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderPendingRequest;
import com.example.orderservice.dto.OrderProcessingRequest;
import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.dto.PlaceOrderRequest;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.integration.feign.InventoryClient;
import com.example.orderservice.integration.feign.PaymentClient;
import com.example.orderservice.integration.kafka.config.KafkaProducerProperties;
import com.example.orderservice.integration.kafka.event.OrderEvent;
import com.example.orderservice.integration.kafka.event.OrderPendingEvent;
import com.example.orderservice.integration.mqtt.MqttGateway;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatusHistory;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderStatusHistoryRepository;
import com.example.orderservice.service.OrderService;
import com.example.springbootmicroservicesframework.exception.AppNotFoundException;
import com.example.springbootmicroservicesframework.integration.kafka.event.Event;
import com.example.springbootmicroservicesframework.utils.AppSecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_ID = "orderId";
    private static final String ORDER_STATUS = "orderStatus";

    OrderRepository orderRepository;
    OrderStatusHistoryRepository orderStatusHistoryRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    InventoryClient inventoryClient;
    PaymentClient paymentClient;
    KafkaProducerProperties kafkaProducerProperties;
    ModelMapper modelMapper;
    ObjectMapper objectMapper;
    OrderMapper orderMapper;
    MqttGateway mqttGateway;
    AppSecurityUtils appSecurityUtils;

    @Transactional
    @Override
    public OrderStatusResponse placeMqtt(PlaceOrderRequest request) throws Exception {
        mqttGateway.sendToMqtt("topic1", "topic1 message");
        mqttGateway.sendToMqtt("topic2", "topic2 message");
        return new OrderStatusResponse();
    }

    @Override
    public OrderStatusResponse getStatus(Long id) throws AppNotFoundException, JsonProcessingException {
        var orderStatusDto = orderRepository.getStatus(id)
                .orElseThrow(() -> new AppNotFoundException(String.format("order id %s", id)));
        var orderDetailStr = orderStatusDto.getOrderDetail();
        var orderDetail = getOrderDetail(orderDetailStr);

        return OrderStatusResponse.builder()
                .orderId(id)
                .orderStatus(orderStatusDto.getOrderStatus())
                .orderDetail(orderDetail)
                .build();
    }

    private ObjectNode getOrderDetail(String orderDetailStr) throws JsonProcessingException {
        var jsonNode = objectMapper.readTree(orderDetailStr);
        var orderDetail = (ObjectNode) jsonNode;
        orderDetail.remove(ORDER_ID);
        orderDetail.remove(ORDER_STATUS);
        return orderDetail;
    }

    @Transactional
    @Override
    public void handleOrderEvent(OrderEvent orderEvent, OrderStatus orderStatus) throws JsonProcessingException {
        var orderDetail = objectMapper.writeValueAsString(orderEvent);
        orderRepository.update(orderEvent.getOrderId(), orderStatus.name(),
                orderDetail, LocalDateTime.now(), AppSecurityUtils.getCurrentAuditor());
        orderStatusHistoryRepository.saveAndFlush(OrderStatusHistory.builder()
                .orderId(orderEvent.getOrderId())
                .status(orderStatus.name())
                .orderDetail(orderDetail)
                .build());

    }

    @Transactional
    @Override
    public OrderStatusResponse placeKafka(PlaceOrderRequest request) throws JsonProcessingException {
        var order = saveOrderPending(request);
        var orderPendingEvent = buildOrderPendingEvent(request, order);
        orderStatusHistoryRepository.saveAndFlush(OrderStatusHistory.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .orderDetail(objectMapper.writeValueAsString(orderPendingEvent))
                .build());
        log.info("send orderPendingEvent {}", orderPendingEvent);
        kafkaTemplate.send(kafkaProducerProperties.getOrderPendingTopicName(), Event.builder()
                .payload(orderPendingEvent)
                .build());
        return OrderStatusResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .build();
    }

    private Order saveOrderPending(PlaceOrderRequest request) {
        var order = Order.builder()
                .status(OrderStatus.PENDING.name())
                .userId(request.getUserId())
                .build();
        orderRepository.saveAndFlush(order);
        return order;
    }

    private OrderPendingEvent buildOrderPendingEvent(PlaceOrderRequest request, Order order) {
        return OrderPendingEvent.builder()
                .accountNumber(String.valueOf(RandomUtils.nextLong(100000000000L, 999999999999L))) //improvement later
                .orderId(order.getId())
                .itemList(request.getItemList().stream()
                        .map(item -> modelMapper.map(item, OrderPendingEvent.Item.class))
                        .toList())
                .build();
    }

    private OrderPendingRequest buildOrderPendingRequest(PlaceOrderRequest request, Order order) {
        return OrderPendingRequest.builder()
                .accountNumber(String.valueOf(RandomUtils.nextLong(100000000000L, 999999999999L))) //improvement later
                .orderId(order.getId())
                .itemList(request.getItemList().stream()
                        .map(item -> modelMapper.map(item, OrderPendingRequest.Item.class))
                        .toList())
                .build();
    }

    @Transactional
    @Override
    public OrderStatusResponse placeOpenFeign(PlaceOrderRequest request) throws JsonProcessingException {
        var order = saveOrderPending(request);
        var orderPendingRequest = buildOrderPendingRequest(request, order);
        var orderStatusHistory = OrderStatusHistory.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .orderDetail(objectMapper.writeValueAsString(orderPendingRequest))
                .build();
        orderStatusHistoryRepository.saveAndFlush(orderStatusHistory);

        log.info("call to inventory-service, orderId {}", orderPendingRequest.getOrderId());
        var orderPendingResponse = inventoryClient.handleOrderPendingOpenFeign(
                appSecurityUtils.getAuthorizationHeader(), orderPendingRequest);
        log.info("receive from inventory-service, orderId {}", orderPendingRequest.getOrderId());
        var orderPendingRes = orderMapper.mapToOrder(order, Order.builder()
                .orderDetail(objectMapper.writeValueAsString(orderPendingResponse))
                .status(orderPendingResponse.getOrderStatus().name())
                .build());
        orderRepository.saveAndFlush(orderPendingRes);
        orderStatusHistoryRepository.saveAndFlush(orderMapper.mapToOrderStatusHistory(orderPendingRes, new OrderStatusHistory()));

        OrderStatus orderStatus = OrderStatus.fromString(orderPendingRes.getStatus());
        if (OrderStatus.PROCESSING == orderStatus) {
            var orderProcessingRequest = OrderProcessingRequest.builder()
                    .accountNumber(orderPendingResponse.getAccountNumber())
                    .orderId(orderPendingResponse.getOrderId())
                    .itemList(orderPendingResponse.getItemList().stream()
                            .map(item -> modelMapper.map(item, OrderProcessingRequest.Item.class))
                            .toList())
                    .build();
            log.info("call to payment-service, orderId {}", orderProcessingRequest.getOrderId());
            var orderProcessingResponse = paymentClient.handleOrderProcessingOpenFeign(
                    appSecurityUtils.getAuthorizationHeader(), orderProcessingRequest);
            log.info("receive from payment-service, orderId {}", orderProcessingRequest.getOrderId());
            var orderProcessingRes = orderMapper.mapToOrder(orderPendingRes, Order.builder()
                    .orderDetail(objectMapper.writeValueAsString(orderProcessingResponse))
                    .status(orderProcessingResponse.getOrderStatus().name())
                    .build());
            orderRepository.saveAndFlush(orderProcessingRes);
            orderStatusHistoryRepository.saveAndFlush(orderMapper.mapToOrderStatusHistory(orderProcessingRes, new OrderStatusHistory()));
            return OrderStatusResponse.builder()
                    .orderId(orderProcessingRes.getId())
                    .orderStatus(orderProcessingRes.getStatus())
                    .orderDetail(getOrderDetail(orderProcessingRes.getOrderDetail()))
                    .build();
        }
        return OrderStatusResponse.builder()
                .orderId(orderPendingRes.getId())
                .orderStatus(orderPendingRes.getStatus())
                .orderDetail(getOrderDetail(orderPendingRes.getOrderDetail()))
                .build();
    }


}

