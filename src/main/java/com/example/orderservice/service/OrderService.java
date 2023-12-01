package com.example.orderservice.service;

import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.dto.PlaceOrderRequest;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.integration.kafka.event.OrderEvent;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderService {
    OrderStatusResponse placeKafka(PlaceOrderRequest request) throws JsonProcessingException;

    OrderStatusResponse placeOpenFeign(PlaceOrderRequest request) throws JsonProcessingException;

    void handleOrderEvent(OrderEvent orderEvent, OrderStatus orderStatus) throws JsonProcessingException;

    OrderStatusResponse getStatus(Long id) throws NotFoundException, JsonProcessingException;

    OrderStatusResponse placeMqtt(PlaceOrderRequest request) throws Exception;
}
