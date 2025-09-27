package com.akash.paymentservice.exception.business;

import com.akash.paymentservice.exception.PaymentServiceBaseException;

public class BusinessException extends PaymentServiceBaseException {

    BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }

}
