package com.akash.paymentservice.event.producer;

import com.akash.events.dto.PaymentCompletionEvent;
import com.akash.events.dto.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, PaymentCompletionEvent> kafkaTemplate;
    private static final String CORRELATION_ID_HEADER = "x-correlation-id";

    public void sendPaymentCompletionEvent(PaymentCompletionEvent event) {
        try {
            String topicName = event.getPaymentStatus()
                    .equals(PaymentStatus.SUCCESS) ? "PAYMENT_SUCCESS_TOPIC" : "PAYMENT_FAILED_TOPIC";
            ProducerRecord<String, PaymentCompletionEvent> record = new ProducerRecord<>(topicName, event);

            String correlationId = MDC.get(CORRELATION_ID_HEADER);
            if (correlationId == null) {
                correlationId = UUID.randomUUID().toString();
            }
            record.headers().add(CORRELATION_ID_HEADER, correlationId.getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(topicName, event);
            log.info("Published PaymentEvent to topic: {} --> {}", topicName, event);
        } finally {
            MDC.clear();
        }
    }

}
