package com.unibank.fraud.service;

import com.unibank.fraud.dto.FraudScoreRequest;
import com.unibank.fraud.dto.FraudScoreResponse;
import com.unibank.fraud.entity.FraudScore;
import com.unibank.fraud.repository.FraudScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudService {

    private final FraudRuleEngine ruleEngine;
    private final FraudScoreRepository fraudScoreRepository;

    public FraudScoreResponse score(FraudScoreRequest request) {
        FraudRuleEngine.RuleResult result = ruleEngine.evaluate(request);
        String riskLevel = ruleEngine.classify(result.score());

        FraudScore entity = FraudScore.builder()
                .paymentRequestId(request.getPaymentRequestId())
                .userId(request.getUserId())
                .score(result.score())
                .riskLevel(riskLevel)
                .reasons(String.join("; ", result.reasons()))
                .modelVersion("rule-based-v1")
                .build();

        FraudScore saved = fraudScoreRepository.save(entity);

        return FraudScoreResponse.builder()
                .id(saved.getId())
                .score(saved.getScore())
                .riskLevel(saved.getRiskLevel())
                .reasons(result.reasons())
                .modelVersion(saved.getModelVersion())
                .build();
    }
}
