package com.unibank.fraud.repository;

import com.unibank.fraud.entity.FraudScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudScoreRepository extends JpaRepository<FraudScore, Long> {
}
