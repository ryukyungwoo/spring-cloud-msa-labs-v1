package com.sesac.orderservice.service;

import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("user not found with id: " + id)
        );
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
