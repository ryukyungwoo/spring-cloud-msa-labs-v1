package com.sesac.orderservice.service;

import com.sesac.orderservice.client.ProductServiceClient;
import com.sesac.orderservice.client.UserServiceClient;
import com.sesac.orderservice.client.dto.ProductDTO;
import com.sesac.orderservice.client.dto.UserDTO;
import com.sesac.orderservice.dto.OrderRequestDTO;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("user not found with id: " + id)
        );
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(OrderRequestDTO request) {

        UserDTO user = userServiceClient.getUserById(request.getUserId());
        if (user == null) throw new RuntimeException("User not found");

        ProductDTO product = productServiceClient.getProductById(request.getProductId());
        if (product == null) throw new RuntimeException("Product not found");

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Quantity not fulfill request Quantity");
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        order.setStatus("COMPLETED");

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long id) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(id);
    }
}
