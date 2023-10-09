package com.example.orderservice.enums;

import java.util.Locale;
import java.util.Optional;

public enum OrderStatus {
    PENDING,       // Order has been placed but not yet processed.
    PROCESSING, // Order is being processed, e.g., payment authorization and item preparation.
    SHIPPED,       // Items have been shipped to the customer.
    DELIVERED,   // Order has been successfully delivered to the customer.
    CANCELLED,  // Order has been cancelled by the customer or admin.
    FAIL_ITEM_NOT_AVAILABLE,
    ;


    public static OrderStatus fromString(String value) {
        try {
            return valueOf(value.toUpperCase(Locale.US));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Invalid value '%s' for OrderStatus (case insensitive)", value), e);
        }
    }

    public static Optional<OrderStatus> fromOptionalString(String value) {
        try {
            return Optional.of(fromString(value));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
