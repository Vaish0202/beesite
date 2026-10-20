package com.unibank.audit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuditLogRequest {

    private Long userId;

    @NotBlank(message = "action is required")
    private String action;

    private String resourceType;

    private String resourceId;

    private String ipAddress;

    private String userAgent;
}