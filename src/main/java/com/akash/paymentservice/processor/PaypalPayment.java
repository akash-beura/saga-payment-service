package com.akash.paymentservice.processor;

import com.akash.paymentservice.exception.business.PaymentFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("PAYPAL")
@Slf4j
public class PaypalPayment implements PaymentStrategy {
    @Override
    public void processPayment(String transactionId, Double amount) {
        log.info("Initiating payment with Paypal for transactionId: {}", transactionId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new PaymentFailedException("Paypal payment failed for tx ID: " + transactionId);
        }
        log.info("Payment with Paypal completed for transactionId: {}", transactionId);
    }
}
