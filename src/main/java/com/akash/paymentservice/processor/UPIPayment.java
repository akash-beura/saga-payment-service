package com.akash.paymentservice.processor;

import com.akash.paymentservice.exception.business.PaymentFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component("UPI")
@Slf4j
public class UPIPayment implements PaymentStrategy {
    @Override
    public void processPayment(String transactionId, Double amount) {
        log.info("Initiating payment with UPI for transactionId: {}", transactionId);
        try {
            Thread.sleep(2000);
            if (new Random().nextInt(10) % 2 == 0) {
                throw new PaymentFailedException("UPI Server is down, please try again");
            }
        } catch (InterruptedException e) {
            throw new PaymentFailedException("UPI Payment failed for tx ID: " + transactionId);
        }
        log.info("Payment with UPI completed for transactionId: {}", transactionId);
    }
}
