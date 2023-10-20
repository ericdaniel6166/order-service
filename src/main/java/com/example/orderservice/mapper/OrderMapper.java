package com.example.orderservice.mapper;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderDetail", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    Order mapToOrder(Order source, @MappingTarget Order target);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(source = "source.id", target = "orderId")
    OrderStatusHistory mapToOrderStatusHistory(Order source, @MappingTarget OrderStatusHistory target);
}
