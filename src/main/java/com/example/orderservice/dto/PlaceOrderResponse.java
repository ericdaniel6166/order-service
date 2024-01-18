package com.example.orderservice.dto;

import com.example.orderservice.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaceOrderResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    OrderStatus orderStatus;
}
