package com.unibank.bankingcoreservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkedAccountResponse {
    private Long id;
    private String bankCode;
    private String bankName;
    private String maskedAccountNumber;
    private String accountType;
    private BigDecimal balanceSnapshot;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
}