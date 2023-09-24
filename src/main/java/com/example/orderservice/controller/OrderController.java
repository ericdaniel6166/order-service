package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.enums.TypeReq;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
//    @TimeLimiter(name = "inventory")
//    @Retry(name = "inventory")
//    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
//        log.info("Placing Order");
//        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
//    }
//
//
//    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
//        log.info("Cannot Place Order, Executing Fallback logic");
//        return CompletableFuture.supplyAsync(() -> "Something went wrong, please order after some time!");
//    }

    @GetMapping
    public String sampleOrder() {
        log.info("Sample order");
        return "Sample Order";
    }

    @PostMapping("/web-client")
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrderWebClient(@RequestBody OrderRequest orderRequest) {
        log.info("Placing Order");
        return orderService.placeOrder(orderRequest, TypeReq.WEB_CLIENT);
    }

    @PostMapping("/feign-client")
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrderFeign(@RequestBody OrderRequest orderRequest) {
        log.info("Placing Order");
        return orderService.placeOrder(orderRequest, TypeReq.FEIGN_CLIENT);
    }
}
