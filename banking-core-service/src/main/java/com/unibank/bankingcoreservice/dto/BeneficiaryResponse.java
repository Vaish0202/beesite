package com.unibank.bankingcoreservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryResponse {
    private Long id;
    private String beneficiaryName;
    private String bankName;
    private String accountNumberMasked;
    private String ifscCode;
    private String status;
    private LocalDateTime createdAt;
}
