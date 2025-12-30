package org.datpham.orderservice.clients;

import org.datpham.orderservice.dto.ProductDTO;
import org.datpham.orderservice.dto.client.request.LockProductReq;
import org.datpham.orderservice.dto.client.request.ProductFilter;

import java.util.List;

public interface ProductClient {
    List<ProductDTO> getProductsByIds (ProductFilter productFilter);
    void lockProductStock(LockProductReq request);
}
