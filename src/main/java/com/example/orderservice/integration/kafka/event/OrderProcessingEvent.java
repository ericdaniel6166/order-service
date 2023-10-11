package com.example.orderservice.integration.kafka.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderProcessingEvent implements OrderEvent {

    Long orderId;
    String accountNumber;
    List<OrderProcessingItem> orderProcessingItemList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    public static class OrderProcessingItem {
        Long productId;
        Integer orderQuantity;
        BigDecimal productPrice;
    }
}
