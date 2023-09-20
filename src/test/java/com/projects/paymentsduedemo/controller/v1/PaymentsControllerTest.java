package com.projects.paymentsduedemo.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.paymentsduedemo.dto.PaymentsDueDto;
import com.projects.paymentsduedemo.exception.PaymentAlreadyExistsException;
import com.projects.paymentsduedemo.exception.PaymentNotFoundException;
import com.projects.paymentsduedemo.exception.PaymentsExceptionHandler;
import com.projects.paymentsduedemo.service.PaymentsService;
import com.projects.paymentsduedemo.validator.PaymentAccessValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.projects.paymentsduedemo.consts.TestConsts.DAYS;
import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT;
import static com.projects.paymentsduedemo.consts.TestConsts.PAYMENT_ID;
import static com.projects.paymentsduedemo.consts.TestConsts.UNTIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentsControllerTest {

    @Mock
    private PaymentsService paymentsService;
    @Mock
    private PaymentAccessValidator paymentAccessValidator;

    @InjectMocks
    private PaymentsController underTest;

    private ObjectMapper mapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(underTest)
                                 .setControllerAdvice(new PaymentsExceptionHandler())
                                 .build();
    }

    @Nested
    class RecordPaymentDueTest {

        @Test
        void shouldRecordPaymentDue() throws Exception {
            // when
            mockMvc.perform(put("/v1/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(PAYMENT)))
                   .andExpect(status().isCreated());

            // then
            then(paymentsService).should().recordPaymentDue(PAYMENT);
        }

        @Test
        void shouldNotRecordPaymentDueWhenPaymentAccessValidatorFails() throws Exception {
            // given
            doThrow(new PaymentAlreadyExistsException(PAYMENT_ID))
                    .when(paymentAccessValidator)
                    .validatePaymentDoesNotExist(PAYMENT_ID);

            // when
            mockMvc.perform(put("/v1/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(PAYMENT)))
                   .andExpect(status().isConflict());

            // then
            then(paymentsService).shouldHaveNoInteractions();
        }
    }

    @Nested
    class GetPaymentsDueWithUntilTest {

        @Test
        void shouldGetPaymentsDueWithUntil() throws Exception {
            // given
            given(paymentsService.getPaymentsDue(UNTIL))
                    .willReturn(PaymentsDueDto.builder().paymentDue(PAYMENT).build());

            // when
            MvcResult mvcResult = mockMvc.perform(get("/v1/payments")
                                                  .param("until", UNTIL.toString()))
                                         .andExpect(status().isOk())
                                         .andReturn();

            // then
            var response = mapper.readValue(mvcResult.getResponse().getContentAsString(), PaymentsDueDto.class);
            assertThat(response.getPaymentsDue()).containsExactly(PAYMENT);
            then(paymentsService).should().getPaymentsDue(UNTIL);
        }

        @Test
        void shouldNotGetPaymentsDueWithoutUntil() throws Exception {
            // when
            mockMvc.perform(get("/v1/payments"))
                   .andExpect(status().isBadRequest());

            // then
            then(paymentsService).shouldHaveNoInteractions();
        }
    }

    @Nested
    class GetPaymentsDueWithDaysTest {

        @Test
        void shouldGetPaymentsDueWithDays() throws Exception {
            // given
            given(paymentsService.getPaymentsDue(DAYS))
                    .willReturn(PaymentsDueDto.builder().paymentDue(PAYMENT).build());

            // when
            MvcResult mvcResult = mockMvc.perform(get("/v1/payments")
                            .param("days", DAYS.toString()))
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            var response = mapper.readValue(mvcResult.getResponse().getContentAsString(), PaymentsDueDto.class);
            assertThat(response.getPaymentsDue()).containsExactly(PAYMENT);
            then(paymentsService).should().getPaymentsDue(DAYS);
        }

        @Test
        void shouldNotGetPaymentsDueWithoutDays() throws Exception {
            // when
            mockMvc.perform(get("/v1/payments"))
                   .andExpect(status().isBadRequest());

            // then
            then(paymentsService).shouldHaveNoInteractions();
        }
    }

    @Nested
    class RecordPaymentDoneTest {

        @Test
        void shouldRecordPaymentDone() throws Exception {
            // when
            mockMvc.perform(delete("/v1/payments/{paymentId}", PAYMENT_ID))
                   .andExpect(status().isNoContent());

            // then
            then(paymentsService).should().recordPaymentDone(PAYMENT_ID);
        }

        @Test
        void shouldNotRecordPaymentDoneWhenPaymentAccessValidatorFails() throws Exception {
            // given
            doThrow(new PaymentNotFoundException(PAYMENT_ID))
                    .when(paymentAccessValidator)
                    .validatePaymentExists(PAYMENT_ID);

            // when
            mockMvc.perform(delete("/v1/payments/{paymentId}", PAYMENT_ID))
                   .andExpect(status().isNotFound());

            // then
            then(paymentsService).shouldHaveNoInteractions();
        }
    }
}
