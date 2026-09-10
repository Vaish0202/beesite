package com.unibank.fraud.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FraudScoreRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    private Long paymentRequestId; // optional — set once payments exist (Day 11)

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than zero")
    private BigDecimal amount;

    @NotNull
    private Boolean newBeneficiary;

    @NotNull
    private Boolean deviceChanged;

    @NotNull
    private Boolean locationChanged;

    @Min(0)
    private int failedLoginCount;

    @Min(0)
    private int transactionVelocity; // number of transactions in a recent window (e.g. last 24h)

    @NotNull
    @DecimalMin(value = "0.0", message = "averageUserAmount cannot be negative")
    private BigDecimal averageUserAmount;
}
