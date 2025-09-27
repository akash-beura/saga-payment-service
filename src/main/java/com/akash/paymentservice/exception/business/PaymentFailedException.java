package com.akash.paymentservice.exception.business;

import com.akash.paymentservice.exception.PaymentServiceBaseException;

public class PaymentFailedException extends PaymentServiceBaseException {

    public PaymentFailedException(String transactionId, String orderId) {
        super("400", "Transaction with ID " + transactionId + " for " + orderId + " failed.");
    }

    public PaymentFailedException(String message) {
        super("1234", message);
    }

}
