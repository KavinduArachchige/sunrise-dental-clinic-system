package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.model.StaffUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffUserRepository
        extends JpaRepository<StaffUser, Long> {

    Optional<StaffUser> findByUsername(
            String username
    );

    boolean existsByUsername(
            String username
    );
}