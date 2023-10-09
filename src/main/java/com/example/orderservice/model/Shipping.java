package com.example.orderservice.model;

import com.example.springbootmicroservicesframework.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "shipping")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Shipping extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Address address;

    @Column(name = "status")
    private String status;

    @Column(name = "tracking_number")
    private String trackingNumber;
}
