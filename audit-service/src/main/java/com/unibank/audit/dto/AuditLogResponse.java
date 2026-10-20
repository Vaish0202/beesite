package com.unibank.audit.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponse {
    private Long id;
    private Long userId;
    private String action;
    private String resourceType;
    private String resourceId;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
}
