package org.datpham.orderservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    @NotEmpty(message = "Product id is required")
    private String productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}
