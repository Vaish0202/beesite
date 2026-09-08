package com.unibank.bankingcoreservice.dto;


import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransactionResponse {
    private Long id;
    private String transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionTime;
    private String status;
}