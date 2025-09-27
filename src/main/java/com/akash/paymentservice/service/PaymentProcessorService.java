package com.akash.paymentservice.service;

import com.akash.paymentservice.event.dto.enums.PaymentMode;
import com.akash.paymentservice.processor.PaymentStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class PaymentProcessorService {

    private final Map<String, PaymentStrategy> paymentStrategies;

    public PaymentProcessorService(Map<String, PaymentStrategy> paymentStrategies) {
        this.paymentStrategies = paymentStrategies;
    }

    void processPayment(UUID transactionId, Double amount, PaymentMode paymentMode) {
        PaymentStrategy paymentStrategy = paymentStrategies.get(paymentMode.name());
        paymentStrategy.processPayment(transactionId.toString(), amount);
    }
}
