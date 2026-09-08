package com.unibank.bankingcoreservice.client.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsentValidationDto {
    private boolean valid;
    private String reason;
}
