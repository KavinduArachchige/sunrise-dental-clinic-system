package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.model.Bill;
import com.sunrise.dentalclinic.service.BillingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BillControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BillingService billingService;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        BillController billController =
                new BillController(
                        billingService
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(
                                billController
                        )
                        .build();
    }


    // =========================================================
    // TEST 01
    // GENERATE BILL
    // =========================================================

    @Test
    void shouldGenerateBillSuccessfully()
            throws Exception {

        Bill bill =
                createBill(
                        1L,
                        "BILL-ABC12345",
                        new BigDecimal("5000.00"),
                        new BigDecimal("1500.00"),
                        new BigDecimal("6500.00")
                );


        when(
                billingService.generateBill(
                        10L
                )
        )
                .thenReturn(
                        bill
                );


        mockMvc.perform(

                        post(
                                "/api/bills/generate/{appointmentId}",
                                10L
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.billId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.billNumber")
                                .value("BILL-ABC12345")
                )

                .andExpect(
                        jsonPath("$.treatmentAmount")
                                .value(5000.00)
                )

                .andExpect(
                        jsonPath("$.consultationFee")
                                .value(1500.00)
                )

                .andExpect(
                        jsonPath("$.totalAmount")
                                .value(6500.00)
                );


        verify(
                billingService,
                times(1)
        )
                .generateBill(
                        10L
                );
    }


    // =========================================================
    // TEST 02
    // GET ALL BILLS
    // =========================================================

    @Test
    void shouldReturnAllBills()
            throws Exception {

        Bill billOne =
                createBill(
                        1L,
                        "BILL-001",
                        new BigDecimal("4000.00"),
                        new BigDecimal("1500.00"),
                        new BigDecimal("5500.00")
                );

        Bill billTwo =
                createBill(
                        2L,
                        "BILL-002",
                        new BigDecimal("6000.00"),
                        new BigDecimal("1500.00"),
                        new BigDecimal("7500.00")
                );


        when(
                billingService.getAllBills()
        )
                .thenReturn(
                        List.of(
                                billOne,
                                billTwo
                        )
                );


        mockMvc.perform(
                        get("/api/bills")
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )

                .andExpect(
                        jsonPath("$[0].billNumber")
                                .value("BILL-001")
                )

                .andExpect(
                        jsonPath("$[1].billNumber")
                                .value("BILL-002")
                );


        verify(
                billingService,
                times(1)
        )
                .getAllBills();
    }


    // =========================================================
    // TEST 03
    // GET ALL BILLS - EMPTY
    // =========================================================

    @Test
    void shouldReturnEmptyBillList()
            throws Exception {

        when(
                billingService.getAllBills()
        )
                .thenReturn(
                        List.of()
                );


        mockMvc.perform(
                        get("/api/bills")
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
    // TEST 04
    // GET BILL BY ID
    // =========================================================

    @Test
    void shouldReturnBillById()
            throws Exception {

        Bill bill =
                createBill(
                        5L,
                        "BILL-005",
                        new BigDecimal("7000.00"),
                        new BigDecimal("1500.00"),
                        new BigDecimal("8500.00")
                );


        when(
                billingService.getBillById(
                        5L
                )
        )
                .thenReturn(
                        bill
                );


        mockMvc.perform(

                        get(
                                "/api/bills/{billId}",
                                5L
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.billId")
                                .value(5)
                )

                .andExpect(
                        jsonPath("$.billNumber")
                                .value("BILL-005")
                )

                .andExpect(
                        jsonPath("$.totalAmount")
                                .value(8500.00)
                );


        verify(
                billingService,
                times(1)
        )
                .getBillById(
                        5L
                );
    }


    // =========================================================
    // TEST 05
    // GET BILL BY APPOINTMENT ID
    // =========================================================

    @Test
    void shouldReturnBillByAppointmentId()
            throws Exception {

        Bill bill =
                createBill(
                        7L,
                        "BILL-007",
                        new BigDecimal("3500.00"),
                        new BigDecimal("1500.00"),
                        new BigDecimal("5000.00")
                );


        when(
                billingService
                        .getBillByAppointment(
                                20L
                        )
        )
                .thenReturn(
                        bill
                );


        mockMvc.perform(

                        get(
                                "/api/bills/appointment/{appointmentId}",
                                20L
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.billId")
                                .value(7)
                )

                .andExpect(
                        jsonPath("$.billNumber")
                                .value("BILL-007")
                )

                .andExpect(
                        jsonPath("$.totalAmount")
                                .value(5000.00)
                );


        verify(
                billingService,
                times(1)
        )
                .getBillByAppointment(
                        20L
                );
    }


    // =========================================================
    // TEST 06
    // INVALID BILL ID FORMAT
    // =========================================================

    @Test
    void shouldReturnBadRequestForInvalidBillId()
            throws Exception {

        mockMvc.perform(
                        get(
                                "/api/bills/not-a-number"
                        )
                )

                .andExpect(
                        status().isBadRequest()
                );


        verify(
                billingService,
                never()
        )
                .getBillById(
                        anyLong()
                );
    }


    // =========================================================
    // TEST 07
    // INVALID APPOINTMENT ID FORMAT
    // =========================================================

    @Test
    void shouldReturnBadRequestForInvalidAppointmentId()
            throws Exception {

        mockMvc.perform(
                        post(
                                "/api/bills/generate/not-a-number"
                        )
                )

                .andExpect(
                        status().isBadRequest()
                );


        verify(
                billingService,
                never()
        )
                .generateBill(
                        anyLong()
                );
    }


    // =========================================================
    // HELPER
    // =========================================================

    private Bill createBill(
            Long billId,
            String billNumber,
            BigDecimal treatmentAmount,
            BigDecimal consultationFee,
            BigDecimal totalAmount
    ) {

        Bill bill =
                new Bill();

        bill.setBillId(
                billId
        );

        bill.setBillNumber(
                billNumber
        );

        bill.setTreatmentAmount(
                treatmentAmount
        );

        bill.setConsultationFee(
                consultationFee
        );

        bill.setTotalAmount(
                totalAmount
        );

        return bill;
    }
}