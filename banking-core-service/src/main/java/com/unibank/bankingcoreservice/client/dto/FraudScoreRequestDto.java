package com.unibank.bankingcoreservice.client.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FraudScoreRequestDto {
    private Long userId;
    private Long paymentRequestId;
    private BigDecimal amount;
    private Boolean newBeneficiary;
    private Boolean deviceChanged;
    private Boolean locationChanged;
    private int failedLoginCount;
    private int transactionVelocity;
    private BigDecimal averageUserAmount;
}
