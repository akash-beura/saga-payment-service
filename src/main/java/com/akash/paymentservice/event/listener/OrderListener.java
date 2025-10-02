package com.akash.paymentservice.event.listener;

import com.akash.events.dto.OrderCreatedEvent;
import com.akash.paymentservice.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@AllArgsConstructor
public class OrderListener {

    private final PaymentService paymentService;

    @KafkaListener(
            topics = "order_created_event",
            groupId = "order-processing-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, OrderCreatedEvent> record) {
        String correlationId = null;
        if (record.headers().lastHeader("x-correlation-id") != null) {
            correlationId = new String(record.headers()
                    .lastHeader("x-correlation-id")
                    .value(), StandardCharsets.UTF_8);
        }
        try {
            MDC.put("x-correlation-id", correlationId);
            log.info("Order created event received: {}", record.value());
            paymentService.processPayment(record.value());
            log.info("Order created event processed");
        } finally {
            MDC.clear();
        }
    }
}
