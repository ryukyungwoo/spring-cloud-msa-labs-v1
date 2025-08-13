package com.sesac.orderservice.event;

import com.sesac.orderservice.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.routing-key.notification}")
    private String notificationRoutingKey;

    @Value("${order.event.routing-key.inventory}")
    private String inventoryRoutingKey;

    public void publishOrderCreated(OrderCreateEvent event) {

        log.info("publish order complete event - order Id: {}", event.getOrderId());

        rabbitTemplate.convertAndSend(exchange, notificationRoutingKey, event);
        log.info("complete publish notification service");

        rabbitTemplate.convertAndSend(exchange, inventoryRoutingKey, event);
        log.info("complete publish product service");

    }
}
