package com.example.orderservice.api;

import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.dto.PlaceOrderRequest;
import com.example.orderservice.service.OrderService;
import com.example.springbootmicroservicesframework.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place-kafka")
    public ResponseEntity<OrderStatusResponse> placeKafka(@RequestBody PlaceOrderRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(orderService.placeKafka(request));
    }

    @PostMapping("/place-open-feign")
    public ResponseEntity<OrderStatusResponse> placeOpenFeign(@RequestBody PlaceOrderRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(orderService.placeOpenFeign(request));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<OrderStatusResponse> getStatus(@PathVariable Long id) throws NotFoundException, JsonProcessingException {
        return ResponseEntity.ok(orderService.getStatus(id));
    }

}
