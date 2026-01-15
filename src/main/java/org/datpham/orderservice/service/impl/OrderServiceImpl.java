package org.datpham.orderservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.datpham.orderservice.clients.ProductClient;
import org.datpham.orderservice.common.OrderStatus;
import org.datpham.orderservice.dto.ProductDTO;
import org.datpham.orderservice.dto.client.request.LockProductItem;
import org.datpham.orderservice.dto.client.request.LockProductReq;
import org.datpham.orderservice.dto.client.request.ProductFilter;
import org.datpham.orderservice.dto.request.CreateOrderRequest;
import org.datpham.orderservice.dto.request.OrderItemRequest;
import org.datpham.orderservice.dto.response.OrderResponse;
import org.datpham.orderservice.entity.Order;
import org.datpham.orderservice.entity.OrderItem;
import org.datpham.orderservice.mapper.OrderMapper;
import org.datpham.orderservice.repository.OrderItemRepository;
import org.datpham.orderservice.repository.OrderRepository;
import org.datpham.orderservice.service.OrderService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    ProductClient productClient;
    OrderItemRepository orderItemRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional()
    public OrderResponse createOrder(CreateOrderRequest request) {
        // --- BƯỚC 1: Lấy thông tin sản phẩm từ Product Service để validate ---
        List<String> productIds = request.getOrderItems().stream()
                .map(OrderItemRequest::getProductId).distinct().collect(Collectors.toList());

        List<ProductDTO> products = productClient.getProductsByIds(new ProductFilter(productIds));

        Map<String, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));

        // --- BƯỚC 2: Khởi tạo Order ---
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(OrderStatus.NEW.name());
        order.setTotalAmount(0L);
        Order savedOrder = orderRepository.save(order);

        long totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // Tạo list item để gửi sang Product Service trừ kho
        List<LockProductItem> lockItems = new ArrayList<>();

        // --- BƯỚC 3: Xử lý từng Item ---
        for (OrderItemRequest orderItemRequest : request.getOrderItems()) {
            ProductDTO productDTO = productMap.get(orderItemRequest.getProductId());

            if (productDTO == null) {
                throw new RuntimeException("Cannot find product with id: " + orderItemRequest.getProductId());
            }

            // Validate tồn kho (Check tạm thời ở phía Order Service)
            if (orderItemRequest.getQuantity() > productDTO.getStock()) {
                throw new RuntimeException("Product " + productDTO.getName() + " is out of stock.");
            }

            long price = productDTO.getPrice();

            // Tạo OrderItem lưu DB
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProductId(orderItemRequest.getProductId());
            item.setPrice(BigDecimal.valueOf(price));
            item.setQuantity(orderItemRequest.getQuantity());

            orderItems.add(item);
            totalAmount += price * orderItemRequest.getQuantity();

            // Tạo LockItem để gửi request lock
            LockProductItem lockItem = new LockProductItem();
            lockItem.setId(orderItemRequest.getProductId());
            lockItem.setQuantity(orderItemRequest.getQuantity());
            lockItems.add(lockItem);
        }

        // --- BƯỚC 4: Lưu xuống DB của Order Service ---
        orderItemRepository.saveAll(orderItems);
        savedOrder.setTotalAmount(totalAmount);
        Order finalOrder = orderRepository.save(savedOrder);


        kafkaTemplate.send("order_created", finalOrder);
        log.info("Order created successfully: {}", finalOrder.getId());

        LockProductReq lockReq = new LockProductReq();
        lockReq.setItems(lockItems);

        try {
            // Gọi API PUT /lock
            productClient.lockProductStock(lockReq);
        } catch (Exception e) {
            // QUAN TRỌNG:
            // Nếu gọi sang Product Service bị lỗi (ví dụ: mạng lỗi, hoặc bên kia check lại thấy hết hàng)
            // Exception này sẽ kích hoạt @Transactional của hàm createOrder
            // -> Toàn bộ Order và OrderItem vừa lưu ở trên sẽ bị Rollback (xóa khỏi DB).
            log.error("Failed to lock stock for orderId: {}. Error: {}", finalOrder.getId(), e.getMessage());
            throw new RuntimeException("Failed to lock product stock. Order cancelled.");
        }

        return orderMapper.toOrderResponse(finalOrder);
    }
}
