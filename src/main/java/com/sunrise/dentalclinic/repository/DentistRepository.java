package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DentistRepository extends JpaRepository<Dentist, Long> {

    Optional<Dentist> findByEmail(String email);

    Optional<Dentist> findByContactNumber(String contactNumber);
}