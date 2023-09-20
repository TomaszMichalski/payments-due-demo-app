package com.projects.paymentsduedemo.service;

import com.projects.paymentsduedemo.util.DaysToDateConverter;
import com.projects.paymentsduedemo.dao.PaymentsDao;
import com.projects.paymentsduedemo.dto.PaymentDto;
import com.projects.paymentsduedemo.dto.PaymentsDueDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentsService {
    private final PaymentsDao paymentsDao;
    private final DaysToDateConverter daysToDateConverter;

    public void recordPaymentDue(PaymentDto payment) {
        log.info("Recording new payment due: {}", payment);
        paymentsDao.recordPaymentDue(payment);
    }

    public PaymentsDueDto getPaymentsDue(LocalDateTime until) {
        log.info("Getting payments due until: {}", until);

        return PaymentsDueDto.builder()
                             .paymentsDue(paymentsDao.getPaymentsDue(until))
                             .build();
    }

    public PaymentsDueDto getPaymentsDue(int days) {
        LocalDateTime until = daysToDateConverter.toNowPlusDays(days);
        return getPaymentsDue(until);
    }

    public void recordPaymentDone(String paymentId) {
        log.info("Recording payment done: {}", paymentId);
        paymentsDao.recordPaymentDone(paymentId);
    }

    public Optional<PaymentDto> getPayment(String paymentId) {
        log.info("Getting payment: {}", paymentId);
        return paymentsDao.getPayment(paymentId);
    }
}
