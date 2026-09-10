package com.unibank.fraud.service;

import com.unibank.fraud.dto.FraudScoreRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class FraudRuleEngine {

    public record RuleResult(int score, List<String> reasons) {}

    public RuleResult evaluate(FraudScoreRequest req) {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        // Rule 1: Large amount relative to user's own average spend
        if (req.getAverageUserAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = req.getAmount().divide(req.getAverageUserAmount(), 2, java.math.RoundingMode.HALF_UP);
            if (ratio.compareTo(new BigDecimal("5")) >= 0) {
                score += 30;
                reasons.add("Amount is " + ratio + "x higher than user's average transaction");
            } else if (ratio.compareTo(new BigDecimal("3")) >= 0) {
                score += 15;
                reasons.add("Amount is significantly higher than user's average transaction");
            }
        } else if (req.getAmount().compareTo(new BigDecimal("50000")) > 0) {
            // No history to compare against, but the amount itself is large
            score += 15;
            reasons.add("Large amount with no prior transaction history for comparison");
        }

        // Rule 2: New beneficiary
        if (Boolean.TRUE.equals(req.getNewBeneficiary())) {
            score += 15;
            reasons.add("Payment to a new/unverified beneficiary");
        }

        // Rule 3: Device changed
        if (Boolean.TRUE.equals(req.getDeviceChanged())) {
            score += 15;
            reasons.add("Transaction initiated from a new device");
        }

        // Rule 4: Location changed
        if (Boolean.TRUE.equals(req.getLocationChanged())) {
            score += 15;
            reasons.add("Transaction location differs from usual pattern");
        }

        // Rule 5: Recent failed login attempts
        if (req.getFailedLoginCount() >= 5) {
            score += 20;
            reasons.add("High number of recent failed login attempts (" + req.getFailedLoginCount() + ")");
        } else if (req.getFailedLoginCount() >= 2) {
            score += 10;
            reasons.add("Recent failed login attempts detected (" + req.getFailedLoginCount() + ")");
        }

        // Rule 6: Transaction velocity (many transactions in a short window)
        if (req.getTransactionVelocity() >= 10) {
            score += 20;
            reasons.add("Unusually high transaction velocity (" + req.getTransactionVelocity() + " recent transactions)");
        } else if (req.getTransactionVelocity() >= 5)
        {
            score += 10;
            reasons.add("Elevated transaction velocity (" + req.getTransactionVelocity() + " recent transactions)");
        }

        // Rule 7: Compounding effect — multiple simultaneous red flags are worse
        // than the sum of their parts (classic fraud pattern: new device + new
        // beneficiary + large amount together is far riskier than any one alone)
        int flagCount = 0;
        if (Boolean.TRUE.equals(req.getNewBeneficiary())) flagCount++;
        if (Boolean.TRUE.equals(req.getDeviceChanged())) flagCount++;
        if (Boolean.TRUE.equals(req.getLocationChanged())) flagCount++;
        if (flagCount >= 3) {
            score += 15;
            reasons.add("Multiple risk indicators present simultaneously");
        }

        score = Math.min(score, 100);

        if (reasons.isEmpty()) {
            reasons.add("No risk indicators detected");
        }

        return new RuleResult(score, reasons);
    }

    public String classify(int score) {
        if (score <= 30) return "LOW";
        if (score <= 60) return "MEDIUM";
        if (score <= 80) return "HIGH";
        return "CRITICAL";
    }
}
