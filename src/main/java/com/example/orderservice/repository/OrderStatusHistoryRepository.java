package com.example.orderservice.repository;

import com.example.orderservice.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
}
