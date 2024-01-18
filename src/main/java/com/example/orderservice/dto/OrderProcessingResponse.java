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
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderProcessingResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    Long orderId;
    String accountNumber;
    BigDecimal totalAmount;
    OrderStatus orderStatus;

}
