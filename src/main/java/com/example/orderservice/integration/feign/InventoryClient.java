package com.example.orderservice.integration.feign;

import com.example.orderservice.dto.OrderPendingRequest;
import com.example.orderservice.dto.OrderPendingResponse;
import com.example.springbootmicroservicesframework.integration.feign.FeignClientConfig;
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
    OrderPendingResponse handleOrderPendingOpenFeign(@RequestBody OrderPendingRequest event);


}
