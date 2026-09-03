package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    Optional<Treatment> findByTreatmentNameIgnoreCase(String treatmentName);
}