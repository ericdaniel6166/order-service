package com.example.orderservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaceOrderRequest {
    Long userId;
    List<Item> itemList;
    Long addressId;
//    PaymentDetail paymentDetail; //improve later

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item {
        Long productId;
        Integer orderQuantity;
    }
}
