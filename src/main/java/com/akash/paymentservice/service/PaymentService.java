package com.akash.paymentservice.service;

import com.akash.paymentservice.event.dto.order.OrderCreatedEvent;
import com.akash.paymentservice.model.Transaction;
import com.akash.paymentservice.model.enums.PaymentStatus;
import com.akash.paymentservice.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final PaymentProcessorService paymentProcessorService;

    @Transactional
    public void processPayment(OrderCreatedEvent orderCreatedEvent) {
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .amount(orderCreatedEvent.getAmount())
                .userId(orderCreatedEvent.getUserId())
                .initiatedAt(LocalDateTime.now())
                .status(PaymentStatus.PROCESSING)
                .paymentMode(orderCreatedEvent.getPaymentMode())
                .build();
        transactionRepository.save(transaction);
        paymentProcessorService.processPayment(transaction.getTransactionId(),
                transaction.getAmount(), orderCreatedEvent.getPaymentMode());
        transaction.setCompletedAt(LocalDateTime.now());
    }
}
