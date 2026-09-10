package com.unibank.fraud.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudScoreResponse {
    private Long id;
    private int score;
    private String riskLevel;
    private List<String> reasons;
    private String modelVersion;
}