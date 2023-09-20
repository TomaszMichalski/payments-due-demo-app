package com.projects.paymentsduedemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentsDueDto {
    @Singular("paymentDue")
    List<PaymentDto> paymentsDue;
}
