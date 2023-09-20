package com.projects.paymentsduedemo.exception;

public class PaymentAlreadyExistsException extends RuntimeException {

    public PaymentAlreadyExistsException(String paymentId) {
        super(String.format("Payment with paymentId=%s already exists", paymentId));
    }
}
