package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.model.AuditLog;
import com.sunrise.dentalclinic.repository.AuditLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(
            AuditLogRepository auditLogRepository
    ) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping
    public List<AuditLog> getAllAuditLogs() {

        return auditLogRepository
                .findAllByOrderByCreatedAtDesc();
    }
}