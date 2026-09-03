package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.model.AuditLog;
import com.sunrise.dentalclinic.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(
            AuditLogRepository auditLogRepository
    ) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(
            String username,
            String action,
            String module,
            String details
    ) {

        AuditLog auditLog =
                new AuditLog();

        auditLog.setUsername(
                username
        );

        auditLog.setAction(
                action
        );

        auditLog.setModule(
                module
        );

        auditLog.setDetails(
                details
        );

        auditLogRepository.save(
                auditLog
        );
    }
}