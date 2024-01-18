package com.example.orderservice.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderStatusDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    String orderStatus;
    //    @JsonIgnore //uncomment
    String orderDetail;

}
