package com.example.orderservice.api;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderStatusResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderStatusResponse> place(@RequestBody OrderRequest request) {
        OrderStatusResponse orderStatusResponse = orderService.place(request);
        return ResponseEntity.ok(orderStatusResponse);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<OrderStatusResponse> getStatus(@PathVariable Long id) throws NotFoundException, JsonProcessingException {
        OrderStatusResponse orderStatusResponse = orderService.getStatus(id);
        return ResponseEntity.ok(orderStatusResponse);
    }

}
