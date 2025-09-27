package com.akash.paymentservice.event.dto.order;

import com.akash.paymentservice.event.dto.enums.OrderStatus;
import com.akash.paymentservice.event.dto.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    private String userId;
    private String orderId;
    private Double amount;
    private OrderStatus status;
    private PaymentMode paymentMode;
    private LocalDateTime createdAt;

}


