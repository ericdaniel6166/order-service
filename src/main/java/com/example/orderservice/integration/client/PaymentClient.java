package com.example.orderservice.integration.client;

import com.example.orderservice.dto.OrderProcessingRequest;
import com.example.orderservice.dto.OrderProcessingResponse;
import com.example.springbootmicroservicesframework.config.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${feign.client.config.payment-client.name}",
        url = "${feign.client.config.payment-client.url}",
        configuration = FeignClientConfig.class)
public interface PaymentClient {
    @GetMapping("/test")
    String test();

    @PostMapping("/handle-order-processing-open-feign")
    OrderProcessingResponse handleOrderProcessingOpenFeign(@RequestBody OrderProcessingRequest event);


}
