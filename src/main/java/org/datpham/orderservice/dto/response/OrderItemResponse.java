package org.datpham.orderservice.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private String id;
    private String productId;
    private BigDecimal price;
    private Integer quantity;
}
