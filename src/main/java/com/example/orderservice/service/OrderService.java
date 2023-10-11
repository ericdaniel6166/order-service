package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.integration.kafka.event.OrderEvent;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderService {
    OrderStatusResponse place(OrderRequest request);

    void handleOrderEvent(OrderEvent itemNotAvailableEvent, OrderStatus failItemNotAvailable) throws JsonProcessingException;

    OrderStatusResponse getStatus(Long id) throws NotFoundException, JsonProcessingException;

}
