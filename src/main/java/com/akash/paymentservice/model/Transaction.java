package com.akash.paymentservice.model;

import com.akash.events.dto.enums.PaymentMode;
import com.akash.paymentservice.model.enums.TransactionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Transaction {

    @Id
    private UUID transactionId;
    private String orderId;
    private String userId;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;

}
