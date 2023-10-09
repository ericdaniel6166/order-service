package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderStatusDto;
import com.example.orderservice.dto.OrderStatusResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "failReason", ignore = true)
    OrderStatusResponse mapToOrderStatusResponse(OrderStatusDto orderStatusDto, Long orderId, @MappingTarget OrderStatusResponse orderStatusResponse);
}
