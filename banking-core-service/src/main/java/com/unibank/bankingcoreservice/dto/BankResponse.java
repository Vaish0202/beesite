package com.unibank.bankingcoreservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponse {
    private Long id;
    private String name;
    private String bankCode;
    private String status;
}