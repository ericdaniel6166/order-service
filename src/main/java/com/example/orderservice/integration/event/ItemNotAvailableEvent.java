package com.example.orderservice.integration.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemNotAvailableEvent implements Serializable {
    static final long serialVersionUID = 21345L;

    Long orderId;

    List<ItemNotAvailable> itemNotAvailableList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    public static class ItemNotAvailable {
        Long productId;
        Integer orderQuantity;
        Integer inventoryQuantity;
    }


}
