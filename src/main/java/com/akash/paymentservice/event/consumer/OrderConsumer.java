package com.akash.paymentservice.event.consumer;

import com.akash.paymentservice.event.dto.order.OrderCreatedEvent;
import com.akash.paymentservice.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class OrderConsumer {

    private final PaymentService paymentService;

    @KafkaListener(
            topics = "order_created_event",
            groupId = "order-processing-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(OrderCreatedEvent orderCreatedEvent) {
        try {
            HashMap<String, String> mdcMap = new HashMap<>();
            mdcMap.put("x-correlation-id", UUID.randomUUID().toString());
            MDC.setContextMap(mdcMap);
            log.info("Order created event received: {}", orderCreatedEvent);
            paymentService.processPayment(orderCreatedEvent);
            log.info("Order created event processed");
        } finally {
            MDC.clear();
        }
    }
}
