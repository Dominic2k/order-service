package org.datpham.orderservice.mapper;

import org.datpham.orderservice.dto.request.CreateOrderRequest;
import org.datpham.orderservice.dto.response.OrderResponse;
import org.datpham.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(CreateOrderRequest request);

    OrderResponse toOrderResponse(Order order);
}
