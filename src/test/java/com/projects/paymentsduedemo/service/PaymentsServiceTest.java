package com.projects.paymentsduedemo.service;

import com.projects.paymentsduedemo.dao.PaymentsDao;
import com.projects.paymentsduedemo.dto.PaymentDto;
import com.projects.paymentsduedemo.dto.PaymentsDueDto;
import com.projects.paymentsduedemo.util.DaysToDateConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.projects.paymentsduedemo.consts.TestConsts.DAYS;
import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT;
import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT_ID;
import static com.projects.paymentsduedemo.consts.TestConsts.UNTIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PaymentsServiceTest {

    @Mock
    private PaymentsDao paymentsDao;
    @Mock
    private DaysToDateConverter daysToDateConverter;

    @InjectMocks
    private PaymentsService underTest;

    @Test
    void shouldRecordPaymentDue() {
        // when
        underTest.recordPaymentDue(PAYMENT);

        // then
        then(paymentsDao).should().recordPaymentDue(PAYMENT);
    }

    @Test
    void shouldGetPaymentsDueWithUntil() {
        // given
        given(paymentsDao.getPaymentsDue(UNTIL))
                .willReturn(List.of(PAYMENT));

        // when
        PaymentsDueDto result = underTest.getPaymentsDue(UNTIL);

        // then
        assertThat(result.getPaymentsDue()).containsExactly(PAYMENT);
        then(paymentsDao).should().getPaymentsDue(UNTIL);
    }

    @Test
    void shouldGetPaymentsDueWithDays() {
        // given
        given(daysToDateConverter.toNowPlusDays(DAYS))
                .willReturn(UNTIL);
        given(paymentsDao.getPaymentsDue(UNTIL))
                .willReturn(List.of(PAYMENT));

        // when
        PaymentsDueDto result = underTest.getPaymentsDue(DAYS);

        // then
        assertThat(result.getPaymentsDue()).containsExactly(PAYMENT);
        then(paymentsDao).should().getPaymentsDue(UNTIL);
    }

    @Test
    void shouldRecordPaymentDone() {
        // when
        underTest.recordPaymentDone(PAYMENT_ID);

        // then
        then(paymentsDao).should().recordPaymentDone(PAYMENT_ID);
    }

    @Test
    void shouldGetPayment() {
        // given
        given(paymentsDao.getPayment(PAYMENT_ID))
                .willReturn(Optional.of(PAYMENT));

        // when
        Optional<PaymentDto> result = underTest.getPayment(PAYMENT_ID);

        // then
        assertThat(result).hasValue(PAYMENT);
        then(paymentsDao).should().getPayment(PAYMENT_ID);
    }
}
