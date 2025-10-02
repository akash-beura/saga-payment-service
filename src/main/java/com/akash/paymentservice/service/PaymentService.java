package com.akash.paymentservice.service;

import com.akash.dto.OrderCreatedEvent;
import com.akash.dto.PaymentCompletionEvent;
import com.akash.dto.enums.PaymentStatus;
import com.akash.paymentservice.event.producer.PaymentEventPublisher;
import com.akash.paymentservice.exception.business.PaymentFailedException;
import com.akash.paymentservice.model.Transaction;
import com.akash.paymentservice.model.enums.TransactionStatus;
import com.akash.paymentservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final PaymentProcessorService paymentProcessorService;
    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
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
        PaymentStatus[] paymentStatus = new PaymentStatus[1];
        try {
            paymentProcessorService.processPayment(transaction.getTransactionId(),
                    transaction.getAmount(), orderCreatedEvent.getPaymentMode());
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setCompletedAt(LocalDateTime.now());
            paymentStatus[0] = PaymentStatus.SUCCESS;
        } catch (PaymentFailedException ex) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setCompletedAt(LocalDateTime.now());
            paymentStatus[0] = PaymentStatus.FAILED;
        }
        publishPaymentEventAfterProcessing(paymentStatus);
    }

    // This will only be called if the transaction above is successfully committed
    private void publishPaymentEventAfterProcessing(PaymentStatus[] paymentStatus) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                paymentEventPublisher.sendPaymentCompletionEvent(
                        PaymentCompletionEvent.builder()
                                .paymentStatus(paymentStatus[0])
                                .build());
            }
        });
    }
}
