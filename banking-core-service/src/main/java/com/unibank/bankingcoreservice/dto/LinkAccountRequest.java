package com.unibank.bankingcoreservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkAccountRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "bankCode is required")
    private String bankCode;

    @NotBlank(message = "customerRef is required")
    private String customerRef;
}
