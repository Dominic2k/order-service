package org.datpham.orderservice.clients.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.datpham.orderservice.clients.ProductClient;
import org.datpham.orderservice.common.BaseResponse;
import org.datpham.orderservice.dto.ProductDTO;
import org.datpham.orderservice.dto.client.request.LockProductReq;
import org.datpham.orderservice.dto.client.request.ProductFilter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClientImpl implements ProductClient {
    private final WebClient.Builder webClientBuilder;

    @Override
    public List<ProductDTO> getProductsByIds(ProductFilter productFilter) {
        //log.info("Calling Product Service to get products by ids: {}", productFilter);
        try {
            BaseResponse<List<ProductDTO>> response = webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8888/v1/products/search")
                    .bodyValue(productFilter)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BaseResponse<List<ProductDTO>>>() {})
                    .block();

            if (response == null || response.getData() == null) {
                //log.error("Received null response or data from Product Service");
                throw new RuntimeException("Cannot get product list from product service");
            }

            return response.getData();
        } catch (WebClientResponseException e) {
            //log.error("Error calling Product Service: Status {}, Body {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error calling Product Service", e);
        } catch (Exception e) {
            //log.error("Unexpected error calling Product Service", e);
            throw new RuntimeException("Unexpected error calling Product Service", e);
        }
    }

    @Override
    public void lockProductStock(LockProductReq request) {
        try {
            webClientBuilder.build()
                    .put()
                    .uri("http://localhost:8888/v1/products/lock")
                    .bodyValue(request)
                    .retrieve()
                    .toBodilessEntity() // Chỉ cần check status 200 OK là được
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error calling Product Service Lock Stock: Status {}, Body {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error when locking product stock: " + e.getMessage());
        }
    }
}
