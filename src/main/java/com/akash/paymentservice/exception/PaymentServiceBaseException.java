package com.akash.paymentservice.exception;

import lombok.Getter;

@Getter
public class PaymentServiceBaseException extends RuntimeException {

    private final String errorCode;

    public PaymentServiceBaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
