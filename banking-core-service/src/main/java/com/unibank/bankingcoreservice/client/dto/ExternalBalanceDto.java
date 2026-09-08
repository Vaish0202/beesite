package com.unibank.bankingcoreservice.client.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalBalanceDto {
    private Long accountId;
    private BigDecimal balance;
    private String currency;
}