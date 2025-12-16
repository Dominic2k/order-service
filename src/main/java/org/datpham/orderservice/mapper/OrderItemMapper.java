package org.datpham.orderservice.mapper;

import org.datpham.orderservice.dto.request.OrderItemRequest;
import org.datpham.orderservice.dto.response.OrderItemResponse;
import org.datpham.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem toOrderItem(OrderItemRequest request);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
