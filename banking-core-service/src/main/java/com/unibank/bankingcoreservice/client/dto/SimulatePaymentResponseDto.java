package com.unibank.bankingcoreservice.client.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimulatePaymentResponseDto {
    private Long transactionId;
    private String status;
    private BigDecimal newBalance;
}
