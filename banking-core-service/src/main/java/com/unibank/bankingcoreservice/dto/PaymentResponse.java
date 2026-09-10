package com.unibank.bankingcoreservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long fromAccountId;
    private Long beneficiaryId;
    private BigDecimal amount;
    private String remarks;
    private String status;
    private Integer fraudScore;
    private String riskLevel;
    private LocalDateTime createdAt;
}
