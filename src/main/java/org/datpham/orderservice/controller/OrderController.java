package org.datpham.orderservice.controller;

import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.datpham.orderservice.common.BaseResponse;
import org.datpham.orderservice.dto.request.CreateOrderRequest;
import org.datpham.orderservice.dto.response.OrderResponse;
import org.datpham.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@Slf4j
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponse>> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok().body(new BaseResponse<>(orderService.createOrder(request), "Create order successfully!", null));
    }
}
