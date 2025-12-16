package org.datpham.orderservice.service.impl;

import lombok.AccessLevel;
import org.datpham.orderservice.dto.request.CreateOrderRequest;
import org.datpham.orderservice.dto.response.OrderResponse;
import org.datpham.orderservice.entity.Order;
import org.datpham.orderservice.entity.OrderItem;
import org.datpham.orderservice.mapper.OrderItemMapper;
import org.datpham.orderservice.mapper.OrderMapper;
import org.datpham.orderservice.repository.OrderRepository;
import org.datpham.orderservice.service.OrderService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderItemMapper orderItemMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = orderMapper.toOrder(request);

        List<OrderItem> orderItems = new ArrayList<>();
        AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);
        Order finalOrder = order;
        request.getOrderItems().forEach(orderItemRequest -> {
            OrderItem orderItem = orderItemMapper.toOrderItem(orderItemRequest);
            orderItem.setOrder(finalOrder);
            orderItems.add(orderItem);

            totalAmount.set(totalAmount.get().add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        });

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount.get().longValue());
        order.setStatus("PENDING");

        order = orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }
}
