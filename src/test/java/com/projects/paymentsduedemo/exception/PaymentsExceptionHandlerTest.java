package com.projects.paymentsduedemo.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT_ID;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentsExceptionHandlerTest {
    private PaymentsExceptionHandler underTest;

    @BeforeEach
    void setUp() {
        underTest = new PaymentsExceptionHandler();
    }

    @Test
    void shouldHandlePaymentAlreadyExistsException() {
        // when
        ResponseEntity<Void> response = underTest.handlePaymentAlreadyExistsException(new PaymentAlreadyExistsException(PAYMENT_ID));

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldHandlePaymentNotFoundException() {
        // when
        ResponseEntity<Void> response = underTest.handlePaymentNotFoundException(new PaymentNotFoundException(PAYMENT_ID));

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
