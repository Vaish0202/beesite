
package com.unibank.bankingcoreservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FdClosureResponse {
    private Long id;
    private Long linkedAccountId;
    private String fdNumberMasked;
    private String reason;
    private String status;
    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}
