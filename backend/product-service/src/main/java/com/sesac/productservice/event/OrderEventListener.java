package com.sesac.productservice.event;

import com.sesac.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventListener {

    private final ProductService productService;

    @RabbitListener(queues = "${order.event.queue.inventory}")
    public void handlerOrderEvent(OrderCreateEvent event) {
        log.info("Listen Order Create Event: {}", event.getOrderId());

        try {
            productService.decreaseStock(event.getProductId(), Math.toIntExact(event.getQuantity()));
            log.info("complete decrease stock: {}", event.getOrderId());
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            log.error("error occur decreasing stock: {}", event.getOrderId());
        }
    }
}
