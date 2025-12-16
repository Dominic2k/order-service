package org.datpham.orderservice.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private String customerId;
    private List<OrderItemRequest> orderItems;
}
