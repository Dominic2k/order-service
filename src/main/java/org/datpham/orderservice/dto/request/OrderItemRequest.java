package org.datpham.orderservice.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    private String productId;
    private BigDecimal price;
    private Integer quantity;
}
