package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);

    boolean existsByDentistDentistIdAndAppointmentDateAndAppointmentTime(
            Long dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );
}