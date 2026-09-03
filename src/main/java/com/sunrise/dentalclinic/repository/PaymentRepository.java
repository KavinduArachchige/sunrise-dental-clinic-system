package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReceiptNumber(String receiptNumber);

    Optional<Payment> findByBillBillId(Long billId);

    boolean existsByBillBillId(Long billId);
}