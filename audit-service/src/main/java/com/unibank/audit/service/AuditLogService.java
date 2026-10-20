package com.unibank.audit.service;

import com.unibank.audit.dto.AuditLogResponse;
import com.unibank.audit.dto.CreateAuditLogRequest;
import com.unibank.audit.entity.AuditLog;
import com.unibank.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogResponse log(CreateAuditLogRequest request) {
        AuditLog entry = AuditLog.builder()
                .userId(request.getUserId())
                .action(request.getAction())
                .resourceType(request.getResourceType())
                .resourceId(request.getResourceId())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .build();

        return toResponse(auditLogRepository.save(entry));
    }

    // Admin-facing — returns everything, newest first. Filtering by user/action/
    // date range is a reasonable future enhancement once the admin UI exists,
    // but out of scope for today per the plan.
    public List<AuditLogResponse> listAll() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .action(log.getAction())
                .resourceType(log.getResourceType())
                .resourceId(log.getResourceId())
                .ipAddress(log.getIpAddress())
                .userAgent(log.getUserAgent())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
