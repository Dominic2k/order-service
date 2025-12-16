package org.datpham.orderservice.controller;

import lombok.AccessLevel;
import org.datpham.orderservice.dto.BaseResponse;
import org.datpham.orderservice.dto.request.CreateOrderRequest;
import org.datpham.orderservice.dto.response.OrderResponse;
import org.datpham.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponse>> createOrder(@RequestBody CreateOrderRequest request) {
        BaseResponse<OrderResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(orderService.createOrder(request));
        baseResponse.setMessage("Order created successfully");
       return ResponseEntity.ok(baseResponse);
    }
}
