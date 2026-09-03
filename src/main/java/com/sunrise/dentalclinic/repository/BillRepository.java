package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findByBillNumber(String billNumber);

    Optional<Bill> findByAppointmentAppointmentId(Long appointmentId);

    boolean existsByAppointmentAppointmentId(Long appointmentId);
}