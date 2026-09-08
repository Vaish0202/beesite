package com.unibank.consent.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsentRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "linkedAccountId is required")
    private Long linkedAccountId;

    @NotBlank(message = "purpose is required")
    private String purpose;

    @NotBlank(message = "dataScope is required")
    private String dataScope;

    @Min(value = 1, message = "validityDays must be at least 1")
    private int validityDays = 90; // default: 90-day consent, caller can override
}