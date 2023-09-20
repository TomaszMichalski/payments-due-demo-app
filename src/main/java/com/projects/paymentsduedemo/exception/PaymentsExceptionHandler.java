package com.projects.paymentsduedemo.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class PaymentsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<Void> handlePaymentAlreadyExistsException(Exception exception) {
        log.error("Exception during request processing: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Void> handlePaymentNotFoundException(Exception exception) {
        log.error("Exception during request processing: {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
