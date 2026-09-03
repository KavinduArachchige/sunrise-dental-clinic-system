package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Appointment;
import com.sunrise.dentalclinic.model.Bill;
import com.sunrise.dentalclinic.model.Treatment;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.repository.TreatmentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BillingService {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final TreatmentRepository treatmentRepository;
    private final AuditLogService auditLogService;

    public BillingService(
            BillRepository billRepository,
            AppointmentRepository appointmentRepository,
            TreatmentRepository treatmentRepository,
            AuditLogService auditLogService
    ) {
        this.billRepository = billRepository;
        this.appointmentRepository = appointmentRepository;
        this.treatmentRepository = treatmentRepository;
        this.auditLogService = auditLogService;
    }

    // =========================================================
    // GENERATE BILL
    // =========================================================

    public Bill generateBill(Long appointmentId) {

        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Appointment not found with ID: " + appointmentId
                                )
                        );

        // Prevent duplicate bills
        if (
                billRepository
                        .existsByAppointmentAppointmentId(
                                appointmentId
                        )
        ) {

            throw new IllegalStateException(
                    "A bill has already been generated for this appointment."
            );
        }

        // Find treatment using treatment type stored in appointment
        Treatment treatment =
                treatmentRepository
                        .findByTreatmentNameIgnoreCase(
                                appointment.getTreatmentType()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Treatment not found: "
                                                + appointment.getTreatmentType()
                                )
                        );

        BigDecimal treatmentAmount =
                treatment.getPrice();

        BigDecimal consultationFee =
                new BigDecimal("1500.00");

        BigDecimal totalAmount =
                treatmentAmount.add(
                        consultationFee
                );

        Bill bill =
                new Bill();

        bill.setBillNumber(
                generateBillNumber()
        );

        bill.setAppointment(
                appointment
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

        bill.setBillDate(
                LocalDateTime.now()
        );

        Bill savedBill =
                billRepository.save(
                        bill
                );

        auditLogService.log(
                getCurrentUsername(),
                "GENERATE_BILL",
                "BILLING",
                "Generated bill "
                        + savedBill.getBillNumber()
                        + " for appointment "
                        + appointment.getAppointmentNumber()
                        + ", patient "
                        + appointment.getPatient().getPatientName()
                        + ", treatment "
                        + appointment.getTreatmentType()
                        + ", total amount Rs. "
                        + savedBill.getTotalAmount()
        );

        return savedBill;
    }

    // =========================================================
    // GET ALL BILLS
    // =========================================================

    public List<Bill> getAllBills() {

        return billRepository.findAll();
    }

    // =========================================================
    // GET BILL BY ID
    // =========================================================

    public Bill getBillById(Long billId) {

        return billRepository
                .findById(billId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bill not found with ID: " + billId
                        )
                );
    }

    // =========================================================
    // GET BILL BY APPOINTMENT
    // =========================================================

    public Bill getBillByAppointment(
            Long appointmentId
    ) {

        return billRepository
                .findByAppointmentAppointmentId(
                        appointmentId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bill not found for appointment ID: "
                                        + appointmentId
                        )
                );
    }

    // =========================================================
    // GENERATE BILL NUMBER
    // =========================================================

    private String generateBillNumber() {

        String random =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        return "BILL-" + random;
    }

    // =========================================================
    // CURRENT LOGGED-IN USER
    // =========================================================

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication != null
                        &&
                        authentication.isAuthenticated()
                        &&
                        authentication.getName() != null
                        &&
                        !authentication
                                .getName()
                                .equalsIgnoreCase(
                                        "anonymousUser"
                                )
        ) {

            return authentication.getName();
        }

        return "SYSTEM";
    }
}