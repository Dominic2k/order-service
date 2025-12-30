package org.datpham.orderservice.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductDTO {
    private String id;
    private String name;
    private String category_id;
    private int price;
    private int stock;
    private int quantity;
}
