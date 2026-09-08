package com.unibank.bankingcoreservice.dto;


import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceResponse {
    private Long accountId;
    private BigDecimal balance;
    private String currency;
}
