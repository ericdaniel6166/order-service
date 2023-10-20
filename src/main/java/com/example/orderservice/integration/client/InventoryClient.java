package com.example.orderservice.integration.client;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.springbootmicroservicesframework.config.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${feign.client.config.inventory-client.name}",
        url = "${feign.client.config.inventory-client.url}",
        configuration = FeignClientConfig.class)
public interface InventoryClient {
    @GetMapping("/test")
    String test();

    @PostMapping("/handle-order-pending-open-feign")
    OrderResponse handleOrderPendingOpenFeign(@RequestBody OrderRequest event);


}
