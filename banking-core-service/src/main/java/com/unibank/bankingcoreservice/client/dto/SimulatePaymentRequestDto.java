package com.unibank.bankingcoreservice.client.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimulatePaymentRequestDto {
    private Long accountId;
    private BigDecimal amount;
    private String description;
}
