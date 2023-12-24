package com.example.orderservice.integration.feign;

import com.example.orderservice.dto.OrderPendingRequest;
import com.example.orderservice.dto.OrderPendingResponse;
import com.example.springbootmicroservicesframework.integration.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.client.config.inventory-client.name}",
        url = "${feign.client.config.inventory-client.url}",
        configuration = FeignClientConfig.class)
public interface InventoryClient {

    @PostMapping("/handle-order-pending-open-feign")
    OrderPendingResponse handleOrderPendingOpenFeign(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody OrderPendingRequest event);


}
