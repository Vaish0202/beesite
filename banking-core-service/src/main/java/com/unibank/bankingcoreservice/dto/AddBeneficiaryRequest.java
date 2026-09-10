package com.unibank.bankingcoreservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBeneficiaryRequest {

    @NotBlank(message = "beneficiaryName is required")
    private String beneficiaryName;

    @NotBlank(message = "bankName is required")
    private String bankName;

    @NotBlank(message = "accountNumber is required")
    private String accountNumber; // raw input — masked before storage, never stored as-is

    @NotBlank(message = "ifscCode is required")
    private String ifscCode;
}
