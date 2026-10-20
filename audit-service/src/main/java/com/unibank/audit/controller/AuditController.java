package com.unibank.audit.controller;

import com.unibank.audit.dto.*;
import com.unibank.audit.service.AuditLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogService auditLogService;

    // Called internally by every other service to record a sensitive action.
    @PostMapping("/api/audit/log")
    public ResponseEntity<AuditLogResponse> log(@Valid @RequestBody CreateAuditLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditLogService.log(request));
    }

    // Admin-facing read endpoint. No role check yet — that's part of the
    // security-hardening pass (Day 36-42) once api-gateway can propagate
    // the authenticated user's roles to downstream services.
    @GetMapping("/api/admin/audit-logs")
    public List<AuditLogResponse> listAll() {
        return auditLogService.listAll();
    }
}