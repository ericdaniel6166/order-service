package com.example.orderservice.model;

import com.example.springbootmicroservicesframework.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "T_ORDER")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Order extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "ORDER_DETAIL", columnDefinition = "TEXT")
    private String orderDetail;

    @Column(name = "TOTAL_AMOUNT", precision = 19, scale = 4)
    private BigDecimal totalAmount;

}
