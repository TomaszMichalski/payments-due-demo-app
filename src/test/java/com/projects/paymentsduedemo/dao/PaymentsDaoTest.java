package com.projects.paymentsduedemo.dao;

import com.projects.paymentsduedemo.dto.PaymentDto;
import com.projects.paymentsduedemo.dto.PaymentEntry;
import com.projects.paymentsduedemo.users.AuthenticatedUserService;
import com.projects.paymentsduedemo.util.PaymentEntryConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT;
import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT_ENTRY;
import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT_ID;
import static com.projects.paymentsduedemo.consts.TestConsts.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PaymentsDaoTest {

    @Mock
    private DynamoDbTable<PaymentEntry> paymentsTable;
    @Mock
    private PaymentEntryConverter paymentEntryConverter;
    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private PaymentsDao underTest;

    @BeforeEach
    void setUp() {
        given(authenticatedUserService.getAuthenticatedUserId())
                .willReturn(USER_ID);
    }

    @Test
    void shouldRecordPaymentDue() {
        // given
        given(paymentEntryConverter.toPaymentEntry(PAYMENT, USER_ID))
                .willReturn(PAYMENT_ENTRY);

        // when
        underTest.recordPaymentDue(PAYMENT);

        // then
        then(paymentsTable).should().putItem(PAYMENT_ENTRY);
    }

    @Test
    void shouldRecordPaymentDone() {
        // given
        Key key = Key.builder()
                     .partitionValue(USER_ID)
                     .sortValue(PAYMENT_ID)
                     .build();

        // when
        underTest.recordPaymentDone(PAYMENT_ID);

        // then
        then(paymentsTable).should().deleteItem(key);
    }

    @Test
    void shouldGetPayment() {
        // given
        Key key = Key.builder()
                     .partitionValue(USER_ID)
                     .sortValue(PAYMENT_ID)
                     .build();
        given(paymentsTable.getItem(key))
                .willReturn(PAYMENT_ENTRY);
        given(paymentEntryConverter.toPayment(PAYMENT_ENTRY))
                .willReturn(PAYMENT);

        // when
        Optional<PaymentDto> result = underTest.getPayment(PAYMENT_ID);

        // then
        assertThat(result).hasValue(PAYMENT);
        then(paymentsTable).should().getItem(key);
    }

    @Test
    void shouldGetPaymentNotFound() {
        // given
        Key key = Key.builder()
                .partitionValue(USER_ID)
                .sortValue(PAYMENT_ID)
                .build();
        given(paymentsTable.getItem(key))
                .willReturn(null);

        // when
        Optional<PaymentDto> result = underTest.getPayment(PAYMENT_ID);

        // then
        assertThat(result).isEmpty();
        then(paymentsTable).should().getItem(key);
    }
}
