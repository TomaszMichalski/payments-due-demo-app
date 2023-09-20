package com.projects.paymentsduedemo.config;

import com.projects.paymentsduedemo.dto.PaymentEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Bean
    public DynamoDbEnhancedClient dynamoDbClient(
            @Value("${dynamodb.region}") String region,
            @Value("${dynamodb.access-key}") String accessKey,
            @Value("${dynamodb.secret-key}") String secretKey,
            @Value("${dynamodb.endpoint}") String endpoint) {
        DynamoDbClient client = DynamoDbClient.builder()
                                              .region(Region.of(region))
                                              .credentialsProvider(
                                                      StaticCredentialsProvider.create(
                                                              AwsBasicCredentials.create(
                                                                      accessKey,
                                                                      secretKey
                                                              )
                                                      )
                                              )
                                              .endpointOverride(URI.create(endpoint))
                                              .build();

        return DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();
    }

    @Bean
    public DynamoDbTable<PaymentEntry> paymentsDbTable(
            @Value("${dynamodb.region}") String region,
            @Value("${dynamodb.access-key}") String accessKey,
            @Value("${dynamodb.secret-key}") String secretKey,
            @Value("${dynamodb.endpoint}") String endpoint,
            @Value("${dynamodb.table-name}") String tableName) {
        return dynamoDbClient(region, accessKey, secretKey, endpoint)
                .table(tableName, TableSchema.fromBean(PaymentEntry.class));
    }
}
