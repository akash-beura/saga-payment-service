package com.akash.paymentservice.event.dto.payment;

import com.akash.paymentservice.event.dto.enums.PaymentMode;
import com.akash.paymentservice.model.enums.PaymentStatus;

import java.time.LocalDateTime;

public abstract class PaymentEvent {

    private String transactionId;
    private String orderId;
    private PaymentStatus status;
    private PaymentMode paymentMode;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
