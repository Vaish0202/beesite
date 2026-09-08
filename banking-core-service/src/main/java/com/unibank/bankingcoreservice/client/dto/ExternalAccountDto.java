package com.unibank.bankingcoreservice.client.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalAccountDto {
    private Long accountId;
    private String customerRef;
    private String maskedAccountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private String status;
}