package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Appointment;
import com.sunrise.dentalclinic.model.Bill;
import com.sunrise.dentalclinic.model.Patient;
import com.sunrise.dentalclinic.model.Payment;
import com.sunrise.dentalclinic.model.PaymentStatus;

import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private PaymentService paymentService;

    private Bill bill;
    private Payment payment;

    @BeforeEach
    void setUp() {

        Patient patient = new Patient();
        patient.setPatientId(1L);
        patient.setPatientName("Nimal Perera");

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setAppointmentNumber("APT-ABC12345");
        appointment.setPatient(patient);

        bill = new Bill();
        bill.setBillId(1L);
        bill.setBillNumber("BILL-ABC12345");
        bill.setAppointment(appointment);
        bill.setTotalAmount(new BigDecimal("6500.00"));

        payment = new Payment();
        payment.setPaymentId(1L);
        payment.setReceiptNumber("REC-ABC12345");
        payment.setBill(bill);
        payment.setPaidAmount(new BigDecimal("6500.00"));
        payment.setPaymentMethod("CARD");
        payment.setPaymentStatus(PaymentStatus.PAID);
    }

    @Test
    void shouldRecordFullPaymentSuccessfully() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(paymentRepository.existsByBillBillId(1L))
                .thenReturn(false);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> {
                    Payment saved = invocation.getArgument(0);
                    saved.setPaymentId(1L);
                    return saved;
                });

        Payment result =
                paymentService.makePayment(
                        1L,
                        new BigDecimal("6500.00"),
                        "CARD"
                );

        assertNotNull(result);

        assertEquals(
                PaymentStatus.PAID,
                result.getPaymentStatus()
        );

        assertEquals(
                new BigDecimal("6500.00"),
                result.getPaidAmount()
        );

        assertEquals(
                "CARD",
                result.getPaymentMethod()
        );

        assertTrue(
                result.getReceiptNumber()
                        .startsWith("REC-")
        );

        assertNotNull(
                result.getPaymentDate()
        );

        verify(auditLogService, times(1))
                .log(
                        anyString(),
                        eq("FULL_PAYMENT"),
                        eq("PAYMENT"),
                        anyString()
                );
    }

    @Test
    void shouldRecordPartialPaymentSuccessfully() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(paymentRepository.existsByBillBillId(1L))
                .thenReturn(false);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0)
                );

        Payment result =
                paymentService.makePayment(
                        1L,
                        new BigDecimal("3000.00"),
                        "CASH"
                );

        assertEquals(
                PaymentStatus.PARTIALLY_PAID,
                result.getPaymentStatus()
        );

        assertEquals(
                new BigDecimal("3000.00"),
                result.getPaidAmount()
        );

        verify(auditLogService, times(1))
                .log(
                        anyString(),
                        eq("PARTIAL_PAYMENT"),
                        eq("PAYMENT"),
                        anyString()
                );
    }

    @Test
    void shouldRejectPaymentWhenBillDoesNotExist() {

        when(billRepository.findById(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                paymentService.makePayment(
                                        99L,
                                        new BigDecimal("1000.00"),
                                        "CASH"
                                )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Bill not found")
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void shouldRejectDuplicatePaymentForSameBill() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(paymentRepository.existsByBillBillId(1L))
                .thenReturn(true);

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                paymentService.makePayment(
                                        1L,
                                        new BigDecimal("6500.00"),
                                        "CARD"
                                )
                );

        assertTrue(
                exception.getMessage()
                        .contains("already been recorded")
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void shouldRejectZeroPayment() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(paymentRepository.existsByBillBillId(1L))
                .thenReturn(false);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                paymentService.makePayment(
                                        1L,
                                        BigDecimal.ZERO,
                                        "CASH"
                                )
                );

        assertTrue(
                exception.getMessage()
                        .contains("greater than zero")
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void shouldRejectNegativePayment() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(paymentRepository.existsByBillBillId(1L))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        paymentService.makePayment(
                                1L,
                                new BigDecimal("-100.00"),
                                "CASH"
                        )
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void shouldRejectNullPaymentAmount() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(paymentRepository.existsByBillBillId(1L))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        paymentService.makePayment(
                                1L,
                                null,
                                "CASH"
                        )
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void shouldRejectPaymentGreaterThanBillTotal() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(paymentRepository.existsByBillBillId(1L))
                .thenReturn(false);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                paymentService.makePayment(
                                        1L,
                                        new BigDecimal("7000.00"),
                                        "CARD"
                                )
                );

        assertTrue(
                exception.getMessage()
                        .contains("cannot exceed")
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void shouldReturnAllPayments() {

        Payment secondPayment = new Payment();

        secondPayment.setPaymentId(2L);
        secondPayment.setReceiptNumber("REC-XYZ12345");

        when(paymentRepository.findAll())
                .thenReturn(
                        List.of(
                                payment,
                                secondPayment
                        )
                );

        List<Payment> result =
                paymentService.getAllPayments();

        assertEquals(
                2,
                result.size()
        );
    }

    @Test
    void shouldReturnPaymentById() {

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        Payment result =
                paymentService.getPaymentById(1L);

        assertEquals(
                "REC-ABC12345",
                result.getReceiptNumber()
        );
    }

    @Test
    void shouldThrowWhenPaymentIdDoesNotExist() {

        when(paymentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        paymentService.getPaymentById(99L)
        );
    }

    @Test
    void shouldReturnPaymentByBillId() {

        when(paymentRepository.findByBillBillId(1L))
                .thenReturn(Optional.of(payment));

        Payment result =
                paymentService.getPaymentByBill(1L);

        assertEquals(
                "REC-ABC12345",
                result.getReceiptNumber()
        );
    }

    @Test
    void shouldThrowWhenPaymentForBillDoesNotExist() {

        when(paymentRepository.findByBillBillId(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        paymentService.getPaymentByBill(99L)
        );
    }
}