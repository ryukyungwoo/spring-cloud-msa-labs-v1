package com.sesac.orderservice.service;

import com.sesac.orderservice.client.ProductServiceClient;
import com.sesac.orderservice.client.UserServiceClient;
import com.sesac.orderservice.client.dto.ProductDTO;
import com.sesac.orderservice.client.dto.UserDTO;
import com.sesac.orderservice.dto.OrderRequestDTO;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.event.OrderCreateEvent;
import com.sesac.orderservice.event.OrderEventPublisher;
import com.sesac.orderservice.facade.UserServiceFacade;
import com.sesac.orderservice.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceFacade userServiceFacade;
    private final Tracer tracer;
    private final OrderEventPublisher publisher;

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

        Span span = tracer.nextSpan()
                          .name("createOrder")
                          .tag("order.userId", request.getUserId())
                          .tag("order.productId", request.getProductId())
                          .start();

        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            UserDTO user = userServiceFacade.getUserWithFallback(request.getUserId());
            if (user == null) throw new RuntimeException("User not found");

            ProductDTO product = productServiceClient.getProductById(request.getProductId());
            if (product == null) throw new RuntimeException("Product not found");

//            if (product.getStockQuantity() < request.getQuantity()) {
//                throw new RuntimeException("Quantity not fulfill request Quantity");
//            }

            Order order = new Order();
            order.setUserId(request.getUserId());
            order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            order.setStatus("COMPLETED");

            OrderCreateEvent event = new OrderCreateEvent(
                    order.getId(),
                    request.getUserId(),
                    request.getProductId(),
                    Long.valueOf(request.getQuantity()),
                    order.getTotalAmount(),
                    LocalDateTime.now()
            );

            publisher.publishOrderCreated(event);

            return orderRepository.save(order);
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }

    public List<Order> getOrdersByUserId(Long id) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(id);
    }
}
