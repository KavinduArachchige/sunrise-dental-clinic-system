package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.Bill;
import com.sunrise.dentalclinic.model.Payment;
import com.sunrise.dentalclinic.model.PaymentStatus;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.repository.PaymentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final AuditLogService auditLogService;

    public PaymentService(
            PaymentRepository paymentRepository,
            BillRepository billRepository,
            AuditLogService auditLogService
    ) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
        this.auditLogService = auditLogService;
    }

    // =========================================================
    // MAKE PAYMENT
    // =========================================================

    public Payment makePayment(
            Long billId,
            BigDecimal paidAmount,
            String paymentMethod
    ) {

        Bill bill =
                billRepository
                        .findById(billId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Bill not found with ID: " + billId
                                )
                        );

        if (
                paymentRepository
                        .existsByBillBillId(
                                billId
                        )
        ) {

            throw new IllegalStateException(
                    "A payment has already been recorded for this bill."
            );
        }

        if (
                paidAmount == null
                        ||
                        paidAmount.compareTo(
                                BigDecimal.ZERO
                        ) <= 0
        ) {

            throw new IllegalArgumentException(
                    "Paid amount must be greater than zero."
            );
        }

        if (
                paidAmount.compareTo(
                        bill.getTotalAmount()
                ) > 0
        ) {

            throw new IllegalArgumentException(
                    "Paid amount cannot exceed the bill total."
            );
        }

        PaymentStatus status;

        if (
                paidAmount.compareTo(
                        bill.getTotalAmount()
                ) == 0
        ) {

            status =
                    PaymentStatus.PAID;

        } else {

            status =
                    PaymentStatus.PARTIALLY_PAID;
        }

        Payment payment =
                new Payment();

        payment.setReceiptNumber(
                generateReceiptNumber()
        );

        payment.setBill(
                bill
        );

        payment.setPaidAmount(
                paidAmount
        );

        payment.setPaymentMethod(
                paymentMethod
        );

        payment.setPaymentStatus(
                status
        );

        payment.setPaymentDate(
                LocalDateTime.now()
        );

        Payment savedPayment =
                paymentRepository.save(
                        payment
                );

        String action;

        if (
                status ==
                        PaymentStatus.PAID
        ) {

            action =
                    "FULL_PAYMENT";

        } else {

            action =
                    "PARTIAL_PAYMENT";
        }

        auditLogService.log(
                getCurrentUsername(),
                action,
                "PAYMENT",
                "Recorded payment "
                        + savedPayment.getReceiptNumber()
                        + " for bill "
                        + bill.getBillNumber()
                        + ", patient "
                        + bill.getAppointment()
                        .getPatient()
                        .getPatientName()
                        + ", amount Rs. "
                        + savedPayment.getPaidAmount()
                        + ", method "
                        + savedPayment.getPaymentMethod()
                        + ", status "
                        + savedPayment.getPaymentStatus()
        );

        return savedPayment;
    }

    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }

    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    public Payment getPaymentById(
            Long paymentId
    ) {

        return paymentRepository
                .findById(paymentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Payment not found with ID: "
                                        + paymentId
                        )
                );
    }

    // =========================================================
    // GET PAYMENT BY BILL
    // =========================================================

    public Payment getPaymentByBill(
            Long billId
    ) {

        return paymentRepository
                .findByBillBillId(
                        billId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Payment not found for bill ID: "
                                        + billId
                        )
                );
    }

    // =========================================================
    // GENERATE RECEIPT NUMBER
    // =========================================================

    private String generateReceiptNumber() {

        return "REC-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
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