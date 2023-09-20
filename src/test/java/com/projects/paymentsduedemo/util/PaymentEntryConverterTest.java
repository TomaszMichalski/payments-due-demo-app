package com.projects.paymentsduedemo.util;

import com.projects.paymentsduedemo.dto.PaymentDto;
import com.projects.paymentsduedemo.dto.PaymentEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT;
import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT_ENTRY;
import static com.projects.paymentsduedemo.consts.TestConsts.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentEntryConverterTest {
    private PaymentEntryConverter underTest;

    @BeforeEach
    void setUp() {
        underTest = new PaymentEntryConverter();
    }

    @Test
    void shouldConvertPaymentEntryToPayment() {
        // when
        PaymentDto result = underTest.toPayment(PAYMENT_ENTRY);

        // then
        assertThat(result).isEqualTo(PAYMENT);
    }

    @Test
    void shouldConvertPaymentToPaymentEntry() {
        // when
        PaymentEntry result = underTest.toPaymentEntry(PAYMENT, USER_ID);

        // then
        assertThat(result).isEqualTo(PAYMENT_ENTRY);
    }
}
