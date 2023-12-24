package com.example.orderservice.integration.feign;

import com.example.orderservice.dto.OrderProcessingRequest;
import com.example.orderservice.dto.OrderProcessingResponse;
import com.example.springbootmicroservicesframework.integration.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.client.config.payment-client.name}",
        url = "${feign.client.config.payment-client.url}",
        configuration = FeignClientConfig.class)
public interface PaymentClient {

    @PostMapping("/handle-order-processing-open-feign")
    OrderProcessingResponse handleOrderProcessingOpenFeign(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody OrderProcessingRequest event);


}
