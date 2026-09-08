package com.unibank.consent.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentResponse {
    private Long id;
    private Long userId;
    private Long linkedAccountId;
    private String purpose;
    private String dataScope;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime revokedAt;
}