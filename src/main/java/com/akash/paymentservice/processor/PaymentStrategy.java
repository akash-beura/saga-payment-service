package com.akash.paymentservice.processor;

@FunctionalInterface
public interface PaymentStrategy {

    void processPayment(String transactionId, Double amount);

}
