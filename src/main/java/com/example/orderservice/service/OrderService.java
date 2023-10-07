package com.example.orderservice.service;

import com.example.orderservice.config.client.InventoryClient;
import com.example.orderservice.config.kafka.KafkaProducerProperties;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.enums.TypeReq;
import com.example.orderservice.event.OrderPlacedEvent;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItem;
import com.example.orderservice.repository.OrderRepository;
import com.example.springbootmicroservicesframework.config.kafka.Event;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderService {

    final OrderRepository orderRepository;

    final KafkaTemplate<String, Object> kafkaTemplate;

    final InventoryClient inventoryClient;

    final KafkaProducerProperties kafkaProducerProperties;

    public String placeOrder(OrderRequest orderRequest, TypeReq typeReq) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemList(orderLineItems);
        List<String> skuCodes = order.getOrderLineItemList().stream()
                .map(OrderLineItem::getSkuCode)
                .toList();
        List<InventoryResponse> inventoryResponseList = new ArrayList<>();
        switch (typeReq) {
            case FEIGN_CLIENT -> inventoryResponseList = inventoryClient.searchInventory(skuCodes);
            default -> {
            }
        }
        boolean allProductsInStock = false;
        if (ObjectUtils.isNotEmpty(inventoryResponseList)) {
            allProductsInStock = inventoryResponseList.stream()
                    .allMatch(InventoryResponse::isInStock);
        }
        if (Boolean.TRUE.equals(allProductsInStock)) {
            orderRepository.saveAndFlush(order);
            kafkaTemplate.send(kafkaProducerProperties.getNotificationTopicName(), Event.builder()
                    .payload(OrderPlacedEvent.builder()
                            .orderNumber(order.getOrderNumber())
                            .build())
                    .build());
            kafkaTemplate.send(kafkaProducerProperties.getInternalTopicName(), Event.builder()
                    .payload(OrderPlacedEvent.builder()
                            .orderNumber(order.getOrderNumber())
                            .build())
                    .build());
            log.info("inventoryResponseList : {}", inventoryResponseList);
            return "Order Placed";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }


    private OrderLineItem mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItem;
    }

}

