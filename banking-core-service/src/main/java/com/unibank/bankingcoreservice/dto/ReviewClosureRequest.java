package com.unibank.bankingcoreservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewClosureRequest {

    @NotNull(message = "reviewerId is required")
    private Long reviewerId;

    private String remarks; // optional note, not persisted separately today — kept for future audit-log wiring
}
