package com.projects.paymentsduedemo.validator;

import com.projects.paymentsduedemo.exception.PaymentAlreadyExistsException;
import com.projects.paymentsduedemo.exception.PaymentNotFoundException;
import com.projects.paymentsduedemo.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentAccessValidator {
    private final PaymentsService paymentsService;

    public void validatePaymentDoesNotExist(String paymentId) {
        if (paymentsService.getPayment(paymentId).isPresent()) {
            throw new PaymentAlreadyExistsException(paymentId);
        }
    }

    public void validatePaymentExists(String paymentId) {
        if (paymentsService.getPayment(paymentId).isEmpty()) {
            throw new PaymentNotFoundException(paymentId);
        }
    }
}
