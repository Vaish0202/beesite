package com.unibank.bankingcoreservice.client.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalTransactionDto {
    private Long id;
    private String transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionTime;
    private String status;
}
