package com.example.orderservice.integration.kafka.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderProcessingEvent implements Serializable, OrderEvent {
    static final long serialVersionUID = 12346L;

    Long orderId;
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
