package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.model.Bill;
import com.sunrise.dentalclinic.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillingService billingService;

    public BillController(BillingService billingService) {
        this.billingService = billingService;
    }

    // Generate bill
    @PostMapping("/generate/{appointmentId}")
    public ResponseEntity<Bill> generateBill(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                billingService.generateBill(appointmentId)
        );
    }

    // Get all bills
    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {

        return ResponseEntity.ok(
                billingService.getAllBills()
        );
    }

    // Get bill by ID
    @GetMapping("/{billId}")
    public ResponseEntity<Bill> getBillById(
            @PathVariable Long billId) {

        return ResponseEntity.ok(
                billingService.getBillById(billId)
        );
    }

    // Get bill using appointment ID
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Bill> getBillByAppointment(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                billingService.getBillByAppointment(appointmentId)
        );
    }
}