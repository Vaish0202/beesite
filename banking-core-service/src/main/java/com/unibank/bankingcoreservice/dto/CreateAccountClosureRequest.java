package com.unibank.bankingcoreservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountClosureRequest {

    @NotNull(message = "linkedAccountId is required")
    private Long linkedAccountId;

    private String reason;

    private String transferBalanceTo;
}