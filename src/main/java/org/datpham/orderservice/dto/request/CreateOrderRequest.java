package org.datpham.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {

    @NotEmpty(message = "Customer id is required")
    private String customerId;

    @NotEmpty(message = "Order items are required")
    @Valid
    private List<OrderItemRequest> orderItems;
}
