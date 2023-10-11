package com.example.orderservice.repository;

import com.example.orderservice.dto.OrderStatusDto;
import com.example.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Query("""
            update Order o 
            set o.status = :status, 
            o.orderDetail = :orderDetail,
            o.lastModifiedDate = :now 
            where o.id = :id
            """)
    void update(Long id, String status, String orderDetail, LocalDateTime now);

    @Modifying
    @Query("""
            update Order o 
            set o.status = :status,
            o.lastModifiedDate = :now 
            where o.id = :id
            """)
    void update(Long id, String status, LocalDateTime now);

    @Query("""
            select new com.example.orderservice.dto.OrderStatusDto(o.status, o.orderDetail) 
            from Order o 
            where o.id = :id
            """)
    Optional<OrderStatusDto> getStatus(Long id);
}
