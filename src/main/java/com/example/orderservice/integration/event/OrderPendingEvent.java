package com.example.orderservice.integration.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPendingEvent implements Serializable {
    static final long serialVersionUID = 21346L;

    Long orderId;
    List<OrderItemDto> orderItemList;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderItemDto {
        Long productId;
        Integer quantity;
    }
}
