package com.example.orderservice.config.client;

import com.example.orderservice.dto.InventoryResponse;
import com.example.springbootmicroservicesframework.config.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${feign.client.config.inventory-client.name}",
        url = "${feign.client.config.inventory-client.url}",
        configuration = FeignClientConfig.class)
public interface InventoryClient {

    @GetMapping("/api/inventory")
    List<InventoryResponse> searchInventory(@RequestParam List<String> skuCode);
}
