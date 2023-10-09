package com.example.orderservice.integration.client;

import com.example.springbootmicroservicesframework.config.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${feign.client.config.inventory-client.name}",
        url = "${feign.client.config.inventory-client.url}",
        configuration = FeignClientConfig.class)
public interface InventoryClient {

}
