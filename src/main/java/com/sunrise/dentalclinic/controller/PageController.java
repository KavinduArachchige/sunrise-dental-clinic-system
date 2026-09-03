package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.model.Bill;
import com.sunrise.dentalclinic.model.Payment;
import com.sunrise.dentalclinic.model.StaffUser;
import com.sunrise.dentalclinic.repository.StaffUserRepository;
import com.sunrise.dentalclinic.service.BillingService;
import com.sunrise.dentalclinic.service.PaymentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
public class PageController {

    private final StaffUserRepository staffUserRepository;
    private final BillingService billingService;
    private final PaymentService paymentService;

    /*
     * Common professional date/time format used
     * for printable clinic documents.
     *
     * Example:
     * 03 Sep 2026 - 08:56 PM
     */
    private static final DateTimeFormatter DOCUMENT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern(
                    "dd MMM yyyy - hh:mm a",
                    Locale.ENGLISH
            );


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PageController(
            StaffUserRepository staffUserRepository,
            BillingService billingService,
            PaymentService paymentService
    ) {

        this.staffUserRepository = staffUserRepository;
        this.billingService = billingService;
        this.paymentService = paymentService;
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    @GetMapping("/")
    public String dashboard(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "dashboard";
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @GetMapping("/login")
    public String login() {

        return "login";
    }


    // =========================================================
    // PATIENTS
    // =========================================================

    @GetMapping("/patients")
    public String patients(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "patients";
    }


    // =========================================================
    // DENTISTS
    // =========================================================

    @GetMapping("/dentists")
    public String dentists(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "dentists";
    }


    // =========================================================
    // APPOINTMENTS
    // =========================================================

    @GetMapping("/appointments")
    public String appointments(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "appointments";
    }


    // =========================================================
    // APPOINTMENT SEARCH
    // =========================================================

    @GetMapping("/appointment-search")
    public String appointmentSearch(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "appointment-search";
    }


    // =========================================================
    // TREATMENTS
    // =========================================================

    @GetMapping("/treatments")
    public String treatments(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "treatments";
    }


    // =========================================================
    // BILLING
    // =========================================================

    @GetMapping("/billing")
    public String billing(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "billing";
    }


    // =========================================================
    // PRINT BILL
    // =========================================================

    @GetMapping("/print/bill/{billId}")
    public String printBill(
            @PathVariable Long billId,
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );


        Bill bill =
                billingService.getBillById(
                        billId
                );


        model.addAttribute(
                "bill",
                bill
        );


        /*
         * LocalDateTime is formatted in Java rather than
         * using JSTL fmt:formatDate.
         *
         * This prevents the LocalDateTime -> java.util.Date
         * conversion error experienced in the cloud deployment.
         */
        String formattedBillDate = "";

        if (bill.getBillDate() != null) {

            formattedBillDate =
                    bill.getBillDate()
                            .format(
                                    DOCUMENT_DATE_TIME_FORMAT
                            );
        }


        model.addAttribute(
                "formattedBillDate",
                formattedBillDate
        );


        return "bill-print";
    }


    // =========================================================
    // PAYMENTS
    // =========================================================

    @GetMapping("/payments")
    public String payments(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "payments";
    }


    // =========================================================
    // PRINT RECEIPT
    // =========================================================

    @GetMapping("/print/receipt/{paymentId}")
    public String printReceipt(
            @PathVariable Long paymentId,
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );


        Payment payment =
                paymentService.getPaymentById(
                        paymentId
                );


        model.addAttribute(
                "payment",
                payment
        );


        /*
         * Professional printable payment date.
         */
        String formattedPaymentDate = "";

        if (payment.getPaymentDate() != null) {

            formattedPaymentDate =
                    payment.getPaymentDate()
                            .format(
                                    DOCUMENT_DATE_TIME_FORMAT
                            );
        }


        model.addAttribute(
                "formattedPaymentDate",
                formattedPaymentDate
        );


        return "receipt-print";
    }


    // =========================================================
    // REPORTS
    // =========================================================

    @GetMapping("/reports")
    public String reports(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "reports";
    }


    // =========================================================
    // STAFF
    // =========================================================

    @GetMapping("/staff")
    public String staff(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "staff";
    }


    // =========================================================
    // AUDIT LOGS
    // =========================================================

    @GetMapping("/audit-logs")
    public String auditLogs(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "audit-logs";
    }


    // =========================================================
    // ACCESS DENIED
    // =========================================================

    @GetMapping("/access-denied")
    public String accessDenied() {

        return "access-denied";
    }


    // =========================================================
    // HELP & USER GUIDE
    // =========================================================

    @GetMapping("/help")
    public String help(
            Model model,
            Principal principal
    ) {

        addLoggedInUserToModel(
                model,
                principal
        );

        return "help";
    }


    // =========================================================
    // COMMON LOGGED-IN USER DATA
    // =========================================================

    private void addLoggedInUserToModel(
            Model model,
            Principal principal
    ) {

        if (principal == null) {
            return;
        }


        StaffUser loggedInUser =
                staffUserRepository
                        .findByUsername(
                                principal.getName()
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Logged-in staff user not found."
                                )
                        );


        model.addAttribute(
                "loggedInUser",
                loggedInUser
        );
    }
}