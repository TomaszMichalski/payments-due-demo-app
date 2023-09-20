package com.projects.paymentsduedemo.exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String paymentId) {
        super(String.format("Payment for paymentId=%s not found", paymentId));
    }
}
