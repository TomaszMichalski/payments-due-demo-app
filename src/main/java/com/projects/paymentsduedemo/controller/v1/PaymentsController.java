package com.projects.paymentsduedemo.controller.v1;

import com.projects.paymentsduedemo.dto.PaymentDto;
import com.projects.paymentsduedemo.dto.PaymentsDueDto;
import com.projects.paymentsduedemo.service.PaymentsService;
import com.projects.paymentsduedemo.validator.PaymentAccessValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@Log4j2
public class PaymentsController {
    private final PaymentsService paymentsService;
    private final PaymentAccessValidator paymentAccessValidator;

    @Operation(summary = "Record new payment due for authenticated user")
    @PutMapping
    public ResponseEntity<Void> recordPaymentDue(
            @RequestBody @Parameter(description = "Payment data") PaymentDto payment) {
        paymentAccessValidator.validatePaymentDoesNotExist(payment.getPaymentId());
        paymentsService.recordPaymentDue(payment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get payments due until specific datettime for authenticated user")
    @GetMapping(params = "until")
    public ResponseEntity<PaymentsDueDto> getPaymentsDue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Datetime in ISO format") LocalDateTime until) {
        return ResponseEntity.ok(paymentsService.getPaymentsDue(until));
    }

    @Operation(summary = "Get payments due in given days from now for authenticated user")
    @GetMapping(params = { "days" })
    public ResponseEntity<PaymentsDueDto> getPaymentsDue(
            @RequestParam @Parameter(description = "Days from now") Integer days) {
        return ResponseEntity.ok(paymentsService.getPaymentsDue(days));
    }

    @Operation(summary = "Record a payment was done for authenticated user")
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> recordPaymentDone(
            @PathVariable @Parameter(description = "ID of the payment") String paymentId) {
        paymentAccessValidator.validatePaymentExists(paymentId);
        paymentsService.recordPaymentDone(paymentId);
        return ResponseEntity.noContent().build();
    }
}
