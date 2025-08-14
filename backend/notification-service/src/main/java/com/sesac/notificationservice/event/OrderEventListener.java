package com.sesac.notificationservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventListener {

    @RabbitListener(queues = "${order.event.queue.notification}")
    public void handlerOrderEvent(OrderCreateEvent event) {
        log.info("Listen Order Create Event: {}", event.getOrderId());

        try {
            Thread.sleep(3000);
            log.info("complete send eMail: {}", event.getOrderId());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            log.error("error occur sending email: {}", event.getOrderId());
        }
    }
}
