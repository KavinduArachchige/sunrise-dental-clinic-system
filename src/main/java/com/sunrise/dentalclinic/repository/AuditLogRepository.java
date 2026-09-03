package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByCreatedAtDesc();

    List<AuditLog> findByUsernameContainingIgnoreCaseOrderByCreatedAtDesc(
            String username
    );

    List<AuditLog> findByModuleContainingIgnoreCaseOrderByCreatedAtDesc(
            String module
    );
}