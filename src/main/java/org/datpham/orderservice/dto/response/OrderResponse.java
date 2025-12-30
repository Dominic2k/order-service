package org.datpham.orderservice.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String customerId;
    private String status;
    private Long totalAmount;
    private List<OrderItemResponse> orderItems;
    private String createdBy;
    private String updatedBy;
    private String createdDate;
    private String updatedDate;
}
