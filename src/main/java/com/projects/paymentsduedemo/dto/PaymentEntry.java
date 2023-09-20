package com.projects.paymentsduedemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import static com.projects.paymentsduedemo.consts.DynamoDbConsts.DUE_DATE_INDEX;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDbBean
public class PaymentEntry {
    String userId;
    String paymentId;
    String name;
    String vendor;
    String amount;
    String currency;
    String due;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    @DynamoDbSortKey
    public String getPaymentId() {
        return paymentId;
    }

    @DynamoDbSecondarySortKey(indexNames = {DUE_DATE_INDEX})
    public String getDue() {
        return due;
    }
}
