package com.example.orderservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    Long userId;
    List<OrderItemDto> orderItemList;
    Long addressId;
//    PaymentDetail paymentDetail; //improve later

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrderItemDto {
        Long productId;
        Integer orderQuantity;
    }
}
