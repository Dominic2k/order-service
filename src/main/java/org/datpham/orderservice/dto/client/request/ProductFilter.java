package org.datpham.orderservice.dto.client.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductFilter {
    private String[] ids;
    private String name;

    public ProductFilter(List<String> productIds) {
        this.ids = productIds.toArray(new String[0]);
    }
}
