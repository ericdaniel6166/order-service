package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.integration.event.ItemNotAvailableEvent;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderService {
    OrderStatusResponse place(OrderRequest request);

    void handleOrderItemNotAvailable(ItemNotAvailableEvent itemNotAvailableEvent) throws JsonProcessingException;

    OrderStatusResponse getStatus(Long id) throws NotFoundException, JsonProcessingException;

}
