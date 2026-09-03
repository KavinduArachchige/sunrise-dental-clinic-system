package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.model.Payment;
import com.sunrise.dentalclinic.model.PaymentStatus;
import com.sunrise.dentalclinic.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    private ObjectMapper objectMapper;


    // =========================================================
    // SETUP
    // =========================================================

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        PaymentController paymentController =
                new PaymentController(
                        paymentService
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(
                                paymentController
                        )
                        .build();

        objectMapper =
                new ObjectMapper();
    }


    // =========================================================
    // TEST 01
    // RECORD FULL PAYMENT
    // =========================================================

    @Test
    void shouldRecordPaymentSuccessfully()
            throws Exception {

        Payment payment =
                createPayment(
                        1L,
                        "REC-ABC12345",
                        new BigDecimal("6500.00"),
                        "CARD",
                        PaymentStatus.PAID
                );


        when(
                paymentService.makePayment(
                        eq(10L),
                        eq(
                                new BigDecimal(
                                        "6500.00"
                                )
                        ),
                        eq("CARD")
                )
        )
                .thenReturn(
                        payment
                );


        Map<String, Object> request =
                Map.of(
                        "paidAmount",
                        "6500.00",
                        "paymentMethod",
                        "CARD"
                );


        mockMvc.perform(

                        post(
                                "/api/payments/bill/{billId}",
                                10L
                        )

                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )

                                .content(
                                        objectMapper
                                                .writeValueAsString(
                                                        request
                                                )
                                )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.paymentId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.receiptNumber")
                                .value(
                                        "REC-ABC12345"
                                )
                )

                .andExpect(
                        jsonPath("$.paidAmount")
                                .value(6500.00)
                )

                .andExpect(
                        jsonPath("$.paymentMethod")
                                .value("CARD")
                )

                .andExpect(
                        jsonPath("$.paymentStatus")
                                .value("PAID")
                );


        verify(
                paymentService,
                times(1)
        )
                .makePayment(
                        eq(10L),
                        eq(
                                new BigDecimal(
                                        "6500.00"
                                )
                        ),
                        eq("CARD")
                );
    }


    // =========================================================
    // TEST 02
    // RECORD PARTIAL PAYMENT
    // =========================================================

    @Test
    void shouldRecordPartialPaymentSuccessfully()
            throws Exception {

        Payment payment =
                createPayment(
                        2L,
                        "REC-PART1234",
                        new BigDecimal("3000.00"),
                        "CASH",
                        PaymentStatus.PARTIALLY_PAID
                );


        when(
                paymentService.makePayment(
                        eq(20L),
                        eq(
                                new BigDecimal(
                                        "3000.00"
                                )
                        ),
                        eq("CASH")
                )
        )
                .thenReturn(
                        payment
                );


        Map<String, Object> request =
                Map.of(
                        "paidAmount",
                        "3000.00",
                        "paymentMethod",
                        "CASH"
                );


        mockMvc.perform(

                        post(
                                "/api/payments/bill/{billId}",
                                20L
                        )

                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )

                                .content(
                                        objectMapper
                                                .writeValueAsString(
                                                        request
                                                )
                                )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.paymentId")
                                .value(2)
                )

                .andExpect(
                        jsonPath("$.paymentStatus")
                                .value(
                                        "PARTIALLY_PAID"
                                )
                )

                .andExpect(
                        jsonPath("$.paidAmount")
                                .value(3000.00)
                );
    }


    // =========================================================
    // TEST 03
    // GET ALL PAYMENTS
    // =========================================================

    @Test
    void shouldReturnAllPayments()
            throws Exception {

        Payment paymentOne =
                createPayment(
                        1L,
                        "REC-001",
                        new BigDecimal("5000.00"),
                        "CASH",
                        PaymentStatus.PAID
                );

        Payment paymentTwo =
                createPayment(
                        2L,
                        "REC-002",
                        new BigDecimal("6500.00"),
                        "CARD",
                        PaymentStatus.PAID
                );


        when(
                paymentService.getAllPayments()
        )
                .thenReturn(
                        List.of(
                                paymentOne,
                                paymentTwo
                        )
                );


        mockMvc.perform(
                        get("/api/payments")
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )

                .andExpect(
                        jsonPath("$[0].receiptNumber")
                                .value("REC-001")
                )

                .andExpect(
                        jsonPath("$[1].receiptNumber")
                                .value("REC-002")
                );


        verify(
                paymentService,
                times(1)
        )
                .getAllPayments();
    }


    // =========================================================
    // TEST 04
    // GET ALL PAYMENTS - EMPTY
    // =========================================================

    @Test
    void shouldReturnEmptyPaymentList()
            throws Exception {

        when(
                paymentService.getAllPayments()
        )
                .thenReturn(
                        List.of()
                );


        mockMvc.perform(
                        get("/api/payments")
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.length()")
                                .value(0)
                );
    }


    // =========================================================
    // TEST 05
    // GET PAYMENT BY ID
    // =========================================================

    @Test
    void shouldReturnPaymentById()
            throws Exception {

        Payment payment =
                createPayment(
                        5L,
                        "REC-005",
                        new BigDecimal("7000.00"),
                        "BANK_TRANSFER",
                        PaymentStatus.PAID
                );


        when(
                paymentService.getPaymentById(
                        5L
                )
        )
                .thenReturn(
                        payment
                );


        mockMvc.perform(

                        get(
                                "/api/payments/{paymentId}",
                                5L
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.paymentId")
                                .value(5)
                )

                .andExpect(
                        jsonPath("$.receiptNumber")
                                .value("REC-005")
                )

                .andExpect(
                        jsonPath("$.paymentMethod")
                                .value(
                                        "BANK_TRANSFER"
                                )
                );


        verify(
                paymentService,
                times(1)
        )
                .getPaymentById(
                        5L
                );
    }


    // =========================================================
    // TEST 06
    // GET PAYMENT BY BILL ID
    // =========================================================

    @Test
    void shouldReturnPaymentByBillId()
            throws Exception {

        Payment payment =
                createPayment(
                        7L,
                        "REC-007",
                        new BigDecimal("4500.00"),
                        "CASH",
                        PaymentStatus.PARTIALLY_PAID
                );


        when(
                paymentService.getPaymentByBill(
                        30L
                )
        )
                .thenReturn(
                        payment
                );


        mockMvc.perform(

                        get(
                                "/api/payments/bill/{billId}",
                                30L
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.paymentId")
                                .value(7)
                )

                .andExpect(
                        jsonPath("$.receiptNumber")
                                .value("REC-007")
                )

                .andExpect(
                        jsonPath("$.paymentStatus")
                                .value(
                                        "PARTIALLY_PAID"
                                )
                );


        verify(
                paymentService,
                times(1)
        )
                .getPaymentByBill(
                        30L
                );
    }


    // =========================================================
    // TEST 07
    // INVALID PAYMENT ID FORMAT
    // =========================================================

    @Test
    void shouldReturnBadRequestForInvalidPaymentId()
            throws Exception {

        mockMvc.perform(

                        get(
                                "/api/payments/not-a-number"
                        )
                )

                .andExpect(
                        status().isBadRequest()
                );


        verify(
                paymentService,
                never()
        )
                .getPaymentById(
                        any()
                );
    }


    // =========================================================
    // TEST 08
    // INVALID BILL ID FORMAT
    // =========================================================

    @Test
    void shouldReturnBadRequestForInvalidBillId()
            throws Exception {

        mockMvc.perform(

                        get(
                                "/api/payments/bill/not-a-number"
                        )
                )

                .andExpect(
                        status().isBadRequest()
                );


        verify(
                paymentService,
                never()
        )
                .getPaymentByBill(
                        any()
                );
    }


    // =========================================================
    // TEST 09
    // INVALID BILL ID DURING PAYMENT
    // =========================================================

    @Test
    void shouldReturnBadRequestForInvalidBillIdWhenMakingPayment()
            throws Exception {

        Map<String, Object> request =
                Map.of(
                        "paidAmount",
                        "5000.00",
                        "paymentMethod",
                        "CASH"
                );


        mockMvc.perform(

                        post(
                                "/api/payments/bill/not-a-number"
                        )

                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )

                                .content(
                                        objectMapper
                                                .writeValueAsString(
                                                        request
                                                )
                                )
                )

                .andExpect(
                        status().isBadRequest()
                );


        verify(
                paymentService,
                never()
        )
                .makePayment(
                        any(),
                        any(),
                        any()
                );
    }


    // =========================================================
    // HELPER METHOD
    // =========================================================

    private Payment createPayment(
            Long paymentId,
            String receiptNumber,
            BigDecimal paidAmount,
            String paymentMethod,
            PaymentStatus paymentStatus
    ) {

        Payment payment =
                new Payment();

        payment.setPaymentId(
                paymentId
        );

        payment.setReceiptNumber(
                receiptNumber
        );

        payment.setPaidAmount(
                paidAmount
        );

        payment.setPaymentMethod(
                paymentMethod
        );

        payment.setPaymentStatus(
                paymentStatus
        );

        return payment;
    }
}