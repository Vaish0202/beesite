package com.unibank.bankingcoreservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmMfaRequest {

    @NotBlank(message = "otp is required")
    private String otp;
}
