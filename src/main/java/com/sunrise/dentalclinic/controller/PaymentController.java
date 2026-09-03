package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.model.Payment;
import com.sunrise.dentalclinic.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/bill/{billId}")
    public ResponseEntity<Payment> makePayment(
            @PathVariable Long billId,
            @RequestBody Map<String, Object> request) {

        BigDecimal paidAmount =
                new BigDecimal(request.get("paidAmount").toString());

        String paymentMethod =
                request.get("paymentMethod").toString();

        Payment payment = paymentService.makePayment(
                billId,
                paidAmount,
                paymentMethod
        );

        return ResponseEntity.ok(payment);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(paymentId)
        );
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<Payment> getPaymentByBill(
            @PathVariable Long billId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByBill(billId)
        );
    }
}