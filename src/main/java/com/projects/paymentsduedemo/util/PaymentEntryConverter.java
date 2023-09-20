package com.projects.paymentsduedemo.util;

import com.projects.paymentsduedemo.dto.PaymentDto;
import com.projects.paymentsduedemo.dto.PaymentEntry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PaymentEntryConverter {

    public PaymentDto toPayment(PaymentEntry paymentEntry) {
        return PaymentDto.builder()
                         .paymentId(paymentEntry.getPaymentId())
                         .name(paymentEntry.getName())
                         .vendor(paymentEntry.getVendor())
                         .amount(new BigDecimal(paymentEntry.getAmount()))
                         .currency(paymentEntry.getCurrency())
                         .due(LocalDateTime.parse(paymentEntry.getDue(), DateTimeFormatter.ISO_DATE_TIME))
                         .build();
    }

    public PaymentEntry toPaymentEntry(PaymentDto payment, String userId) {
        return PaymentEntry.builder()
                           .userId(userId)
                           .paymentId(payment.getPaymentId())
                           .name(payment.getName())
                           .vendor(payment.getVendor())
                           .amount(payment.getAmount().toPlainString())
                           .currency(payment.getCurrency())
                           .due(payment.getDue().toString())
                           .build();
    }
}
