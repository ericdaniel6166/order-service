package com.example.orderservice.api;

import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.dto.PlaceOrderRequest;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.service.OrderService;
import com.example.springbootmicroservicesframework.exception.AppNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderApi {

    private final OrderService orderService;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/place-mqtt")
    public ResponseEntity<OrderStatusResponse> placeMqtt(@RequestBody PlaceOrderRequest request) throws Exception {
        return ResponseEntity.ok(orderService.placeMqtt(request));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/place-kafka")
    public ResponseEntity<OrderStatusResponse> placeKafka(@RequestBody PlaceOrderRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(orderService.placeKafka(request));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @CircuitBreaker(name = "place-order", fallbackMethod = "placeOpenFeignFallbackMethod")
    @TimeLimiter(name = "place-order")
    @Retry(name = "place-order")
    @PostMapping("/place-open-feign")
    public CompletionStage<ResponseEntity<OrderStatusResponse>> placeOpenFeign(@RequestBody PlaceOrderRequest request) throws JsonProcessingException {
        return CompletableFuture.completedFuture(ResponseEntity.ok(orderService.placeOpenFeign(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/status/{id}")
    public ResponseEntity<OrderStatusResponse> getStatus(@PathVariable Long id) throws AppNotFoundException, JsonProcessingException {
        return ResponseEntity.ok(orderService.getStatus(id));
    }

    public CompletionStage<ResponseEntity<OrderStatusResponse>> placeOpenFeignFallbackMethod(PlaceOrderRequest request, RuntimeException runtimeException) {
        log.info("placeOpenFeignFallbackMethod, runtimeException: {}", runtimeException.getMessage());
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(OrderStatusResponse.builder()
                .orderStatus(OrderStatus.ORDER_SERVICE_UNAVAILABLE.name())
                .orderDetail(runtimeException.getMessage())
                .build()));
    }

}
