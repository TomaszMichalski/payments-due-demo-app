package com.projects.paymentsduedemo.dao;

import com.projects.paymentsduedemo.dto.PaymentDto;
import com.projects.paymentsduedemo.dto.PaymentEntry;
import com.projects.paymentsduedemo.users.AuthenticatedUserService;
import com.projects.paymentsduedemo.util.PaymentEntryConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.projects.paymentsduedemo.consts.DynamoDbConsts.DUE_DATE_INDEX;

@Component
@RequiredArgsConstructor
public class PaymentsDao {
    private final DynamoDbTable<PaymentEntry> paymentsTable;
    private final PaymentEntryConverter paymentEntryConverter;
    private final AuthenticatedUserService authenticatedUserService;

    public void recordPaymentDue(PaymentDto payment) {
        String authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();
        paymentsTable.putItem(paymentEntryConverter.toPaymentEntry(payment, authenticatedUserId));
    }

    public List<PaymentDto> getPaymentsDue(LocalDateTime until) {
        String authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();

        Key key = Key.builder()
                     .partitionValue(authenticatedUserId)
                     .sortValue(until.toString())
                     .build();

        QueryConditional queryConditional = QueryConditional.sortLessThanOrEqualTo(key);

        return paymentsTable.index(DUE_DATE_INDEX)
                            .query(queryConditional)
                            .stream()
                            .flatMap(page -> page.items().stream())
                            .map(paymentEntryConverter::toPayment)
                            .collect(Collectors.toList());
    }

    public void recordPaymentDone(String paymentId) {
        String authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();

        Key key = Key.builder()
                     .partitionValue(authenticatedUserId)
                     .sortValue(paymentId)
                     .build();

        paymentsTable.deleteItem(key);
    }

    public Optional<PaymentDto> getPayment(String paymentId) {
        String authenticatedUserId = authenticatedUserService.getAuthenticatedUserId();

        Key key = Key.builder()
                     .partitionValue(authenticatedUserId)
                     .sortValue(paymentId)
                     .build();

        return Optional.ofNullable(paymentsTable.getItem(key))
                       .map(paymentEntryConverter::toPayment);
    }
}
