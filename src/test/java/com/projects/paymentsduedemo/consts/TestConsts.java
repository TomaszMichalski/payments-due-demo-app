package com.projects.paymentsduedemo.consts;

import com.projects.paymentsduedemo.dto.PaymentDto;
import com.projects.paymentsduedemo.dto.PaymentEntry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConsts {
    public static final String PAYMENT_ID = "P_001";

    public static final LocalDateTime UNTIL = LocalDateTime.of(2023, 10, 20, 12, 34, 56);
    public static final Integer DAYS = 30;

    private static final String PAYMENT_NAME = "Iphone 15";
    private static final String VENDOR_NAME = "Apple";
    private static final BigDecimal AMOUNT = new BigDecimal("999.00");
    private static final String AMOUNT_STRING = "999.00";
    private static final String CURRENCY = "USD";

    public static final LocalDateTime DUE_DATE = LocalDateTime.of(2023, 10, 12, 12, 34, 56);
    public static final String DUE_DATE_STRING = "2023-10-12T12:34:56";

    public static final String USER_ID = "U_001";

    public static final PaymentDto PAYMENT = PaymentDto.builder()
                                                       .paymentId(PAYMENT_ID)
                                                       .name(PAYMENT_NAME)
                                                       .vendor(VENDOR_NAME)
                                                       .amount(AMOUNT)
                                                       .currency(CURRENCY)
                                                       .due(DUE_DATE)
                                                       .build();
    public static final PaymentEntry PAYMENT_ENTRY = PaymentEntry.builder()
                                                                 .userId(USER_ID)
                                                                 .paymentId(PAYMENT_ID)
                                                                 .name(PAYMENT_NAME)
                                                                 .vendor(VENDOR_NAME)
                                                                 .amount(AMOUNT_STRING)
                                                                 .currency(CURRENCY)
                                                                 .due(DUE_DATE_STRING)
                                                                 .build();
}
