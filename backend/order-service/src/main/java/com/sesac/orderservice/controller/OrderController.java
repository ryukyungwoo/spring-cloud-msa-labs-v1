package com.sesac.orderservice.controller;

import com.sesac.orderservice.dto.OrderRequestDTO;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "get Order", description = "find Order by id")
    public ResponseEntity<List<Order>> getOrders() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.findAll());
        }  catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "get Order", description = "find Order by id")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.findById(id));
        }  catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(OrderRequestDTO request) {
        try {
            Order order = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
