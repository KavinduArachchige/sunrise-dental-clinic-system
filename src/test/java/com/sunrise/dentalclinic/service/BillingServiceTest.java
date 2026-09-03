package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Appointment;
import com.sunrise.dentalclinic.model.Bill;
import com.sunrise.dentalclinic.model.Patient;
import com.sunrise.dentalclinic.model.Treatment;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.repository.TreatmentRepository;

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
class BillingServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private BillingService billingService;

    private Appointment appointment;
    private Treatment treatment;
    private Bill bill;

    @BeforeEach
    void setUp() {

        Patient patient = new Patient();
        patient.setPatientId(1L);
        patient.setPatientName("Nimal Perera");
        patient.setContactNumber("0771234567");
        patient.setAddress("Homagama");

        appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setAppointmentNumber("APT-ABC12345");
        appointment.setTreatmentType("Dental Cleaning");
        appointment.setPatient(patient);

        treatment = new Treatment();
        treatment.setTreatmentId(1L);
        treatment.setTreatmentName("Dental Cleaning");
        treatment.setPrice(new BigDecimal("5000.00"));

        bill = new Bill();
        bill.setBillId(1L);
        bill.setBillNumber("BILL-ABC12345");
        bill.setAppointment(appointment);
        bill.setTreatmentAmount(new BigDecimal("5000.00"));
        bill.setConsultationFee(new BigDecimal("1500.00"));
        bill.setTotalAmount(new BigDecimal("6500.00"));
    }

    @Test
    void shouldGenerateBillSuccessfully() {

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        when(billRepository.existsByAppointmentAppointmentId(1L))
                .thenReturn(false);

        when(treatmentRepository.findByTreatmentNameIgnoreCase("Dental Cleaning"))
                .thenReturn(Optional.of(treatment));

        when(billRepository.save(any(Bill.class)))
                .thenAnswer(invocation -> {
                    Bill saved = invocation.getArgument(0);
                    saved.setBillId(1L);
                    return saved;
                });

        Bill result = billingService.generateBill(1L);

        assertNotNull(result);

        assertEquals(
                new BigDecimal("5000.00"),
                result.getTreatmentAmount()
        );

        assertEquals(
                new BigDecimal("1500.00"),
                result.getConsultationFee()
        );

        assertEquals(
                new BigDecimal("6500.00"),
                result.getTotalAmount()
        );

        assertTrue(
                result.getBillNumber().startsWith("BILL-")
        );

        assertNotNull(result.getBillDate());

        verify(billRepository, times(1))
                .save(any(Bill.class));

        verify(auditLogService, times(1))
                .log(
                        anyString(),
                        eq("GENERATE_BILL"),
                        eq("BILLING"),
                        anyString()
                );
    }

    @Test
    void shouldRejectBillWhenAppointmentDoesNotExist() {

        when(appointmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billingService.generateBill(99L)
                );

        assertTrue(
                exception.getMessage()
                        .contains("Appointment not found")
        );

        verify(billRepository, never())
                .save(any(Bill.class));
    }

    @Test
    void shouldRejectDuplicateBillForSameAppointment() {

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        when(billRepository.existsByAppointmentAppointmentId(1L))
                .thenReturn(true);

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> billingService.generateBill(1L)
                );

        assertTrue(
                exception.getMessage()
                        .contains("already been generated")
        );

        verify(billRepository, never())
                .save(any(Bill.class));
    }

    @Test
    void shouldRejectBillWhenTreatmentDoesNotExist() {

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        when(billRepository.existsByAppointmentAppointmentId(1L))
                .thenReturn(false);

        when(treatmentRepository.findByTreatmentNameIgnoreCase("Dental Cleaning"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billingService.generateBill(1L)
                );

        assertTrue(
                exception.getMessage()
                        .contains("Treatment not found")
        );

        verify(billRepository, never())
                .save(any(Bill.class));
    }

    @Test
    void shouldReturnAllBills() {

        Bill secondBill = new Bill();
        secondBill.setBillId(2L);
        secondBill.setBillNumber("BILL-XYZ12345");

        when(billRepository.findAll())
                .thenReturn(List.of(bill, secondBill));

        List<Bill> result =
                billingService.getAllBills();

        assertEquals(2, result.size());

        verify(billRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnBillById() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        Bill result =
                billingService.getBillById(1L);

        assertEquals(
                "BILL-ABC12345",
                result.getBillNumber()
        );
    }

    @Test
    void shouldThrowWhenBillIdDoesNotExist() {

        when(billRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> billingService.getBillById(99L)
        );
    }

    @Test
    void shouldReturnBillByAppointmentId() {

        when(billRepository.findByAppointmentAppointmentId(1L))
                .thenReturn(Optional.of(bill));

        Bill result =
                billingService.getBillByAppointment(1L);

        assertEquals(
                "BILL-ABC12345",
                result.getBillNumber()
        );
    }

    @Test
    void shouldThrowWhenBillForAppointmentDoesNotExist() {

        when(billRepository.findByAppointmentAppointmentId(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> billingService.getBillByAppointment(99L)
        );
    }
}