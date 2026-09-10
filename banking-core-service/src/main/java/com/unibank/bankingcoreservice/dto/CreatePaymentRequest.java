package com.unibank.bankingcoreservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    @NotNull(message = "fromAccountId is required")
    private Long fromAccountId;

    @NotNull(message = "beneficiaryId is required")
    private Long beneficiaryId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than zero")
    private BigDecimal amount;

    private String remarks;

    // Temporary caller-supplied risk signals. We don't have real device
    // fingerprinting or session tracking yet (that needs the frontend +
    // audit-service), so these let us exercise the fraud pipeline honestly
    // today. Default to false/0 when omitted. Replaced with real signals
    // once the frontend (Day 22+) and audit-service (Day 12) exist.
    private Boolean deviceChanged;
    private Boolean locationChanged;
    private Integer failedLoginCount;
}
