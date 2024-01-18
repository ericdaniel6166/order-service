package com.example.orderservice.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderStatusResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    Long orderId;
    String orderStatus;
    //    @JsonIgnore //uncomment
    Object orderDetail;

}
