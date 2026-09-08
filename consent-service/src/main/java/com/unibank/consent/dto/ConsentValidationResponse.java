package com.unibank.consent.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentValidationResponse {
    private boolean valid;
    private String reason; // populated only when valid = false, e.g. "EXPIRED", "REVOKED", "NOT_FOUND"
}
