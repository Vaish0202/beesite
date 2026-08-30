package com.unibank.mocksbi.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulatePaymentResponse {
    private Long transactionId;
    private String status;
    private BigDecimal newBalance;
}
