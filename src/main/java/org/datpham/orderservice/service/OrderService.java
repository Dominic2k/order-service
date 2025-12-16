package org.datpham.orderservice.service;

import org.datpham.orderservice.dto.request.CreateOrderRequest;
import org.datpham.orderservice.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
}
