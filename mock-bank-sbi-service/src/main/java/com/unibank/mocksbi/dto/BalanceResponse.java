package com.unibank.mocksbi.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceResponse {
    private Long accountId;
    private BigDecimal balance;
    private String currency;
}