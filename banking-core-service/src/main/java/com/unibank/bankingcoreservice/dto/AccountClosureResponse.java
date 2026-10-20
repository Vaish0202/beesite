package com.unibank.bankingcoreservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountClosureResponse {
    private Long id;
    private Long linkedAccountId;
    private String reason;
    private String transferBalanceTo;
    private String status;
    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}
