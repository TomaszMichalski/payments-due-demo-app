package com.projects.paymentsduedemo.validator;

import com.projects.paymentsduedemo.exception.PaymentAlreadyExistsException;
import com.projects.paymentsduedemo.exception.PaymentNotFoundException;
import com.projects.paymentsduedemo.service.PaymentsService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT;
import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentAccessValidatorTest {

    @Mock
    private PaymentsService paymentsService;

    @InjectMocks
    private PaymentAccessValidator underTest;

    @Nested
    class ValidatePaymentDoesNotExistTest {

        @Test
        void shouldThrowWhenPaymentExists() {
            // given
            given(paymentsService.getPayment(PAYMENT_ID))
                    .willReturn(Optional.of(PAYMENT));

            // when
            Throwable caught = catchThrowable(() -> underTest.validatePaymentDoesNotExist(PAYMENT_ID));

            // then
            assertThat(caught).isExactlyInstanceOf(PaymentAlreadyExistsException.class);
        }

        @Test
        void shouldNotThrowWhenPaymentDoesNotExist() {
            // given
            given(paymentsService.getPayment(PAYMENT_ID))
                    .willReturn(Optional.empty());

            // when
            underTest.validatePaymentDoesNotExist(PAYMENT_ID);

            // then no exception is thrown
        }
    }

    @Nested
    class ValidatePaymentExistsTest {

        @Test
        void shouldThrowWhenPaymentDoesNotExist() {
            // given
            given(paymentsService.getPayment(PAYMENT_ID))
                    .willReturn(Optional.empty());

            // when
            Throwable caught = catchThrowable(() -> underTest.validatePaymentExists(PAYMENT_ID));

            // then
            assertThat(caught).isExactlyInstanceOf(PaymentNotFoundException.class);
        }

        @Test
        void shouldNotThrowWhenPaymentExists() {
            // given
            given(paymentsService.getPayment(PAYMENT_ID))
                    .willReturn(Optional.of(PAYMENT));

            // when
            underTest.validatePaymentExists(PAYMENT_ID);

            // then no exception is thrown
        }
    }
}
