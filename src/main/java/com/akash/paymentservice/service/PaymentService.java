package com.akash.paymentservice.service;

import com.akash.events.dto.OrderCreatedEvent;
import com.akash.events.dto.PaymentCompletionEvent;
import com.akash.events.dto.enums.PaymentStatus;
import com.akash.paymentservice.event.producer.PaymentProducer;
import com.akash.paymentservice.exception.business.PaymentFailedException;
import com.akash.paymentservice.model.Transaction;
import com.akash.paymentservice.model.enums.TransactionStatus;
import com.akash.paymentservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final PaymentProcessorService paymentProcessorService;
    private final PaymentProducer paymentProducer;

    public void processPayment(OrderCreatedEvent orderCreatedEvent) {
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .orderId(orderCreatedEvent.getOrderId())
                .amount(orderCreatedEvent.getAmount())
                .userId(orderCreatedEvent.getUserId())
                .initiatedAt(LocalDateTime.now())
                .status(TransactionStatus.PROCESSING)
                .paymentMode(orderCreatedEvent.getPaymentMode())
                .build();
        transactionRepository.save(transaction);
        try {
            paymentProcessorService.processPayment(transaction.getTransactionId(),
                    transaction.getAmount(), orderCreatedEvent.getPaymentMode());
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setCompletedAt(LocalDateTime.now());
            paymentProducer.sendPaymentCompletionEvent(PaymentCompletionEvent.builder()
                    .paymentStatus(PaymentStatus.SUCCESS)
                    .build());
        } catch (PaymentFailedException ex) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setCompletedAt(LocalDateTime.now());
            paymentProducer.sendPaymentCompletionEvent(PaymentCompletionEvent.builder()
                    .paymentStatus(PaymentStatus.FAILED)
                    .build());
        }
    }
}
