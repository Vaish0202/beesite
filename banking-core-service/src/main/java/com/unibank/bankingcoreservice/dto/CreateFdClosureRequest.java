package com.unibank.bankingcoreservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFdClosureRequest {

    @NotNull(message = "linkedAccountId is required")
    private Long linkedAccountId;

    @NotBlank(message = "fdNumber is required")
    private String fdNumber; // raw input, masked before storage

    private String reason;
}
