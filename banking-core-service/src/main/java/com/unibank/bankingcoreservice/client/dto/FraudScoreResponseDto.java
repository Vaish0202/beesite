package com.unibank.bankingcoreservice.client.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FraudScoreResponseDto {
    private Long id;
    private int score;
    private String riskLevel;
    private List<String> reasons;
    private String modelVersion;
}
