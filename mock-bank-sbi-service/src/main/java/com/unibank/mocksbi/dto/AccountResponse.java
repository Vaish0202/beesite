package com.unibank.mocksbi.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long accountId;
    private String customerRef;
    private String maskedAccountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private String status;
}