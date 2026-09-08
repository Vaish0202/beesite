package com.unibank.consent.repository;

import com.unibank.consent.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsentRepository extends JpaRepository<Consent, Long> {
    List<Consent> findByUserId(Long userId);
    Optional<Consent> findByIdAndUserId(Long id, Long userId);

    // Used for consent validation — the most recent active consent for a given
    // account, regardless of which user is asking. banking-core-service will
    // call the /validate endpoint with just linkedAccountId (Day 9).
    Optional<Consent> findFirstByLinkedAccountIdAndStatusOrderByIssuedAtDesc(Long linkedAccountId, String status);
}