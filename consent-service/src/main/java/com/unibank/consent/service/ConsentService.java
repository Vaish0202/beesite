package com.unibank.consent.service;

import com.unibank.consent.dto.*;
import com.unibank.consent.entity.Consent;
import com.unibank.consent.exception.ApiException;
import com.unibank.consent.repository.ConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepository consentRepository;

    public ConsentResponse createConsent(CreateConsentRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Consent consent = Consent.builder()
                .userId(request.getUserId())
                .linkedAccountId(request.getLinkedAccountId())
                .purpose(request.getPurpose())
                .dataScope(request.getDataScope())
                .status("ACTIVE")
                .issuedAt(now)
                .expiresAt(now.plusDays(request.getValidityDays()))
                .build();

        Consent saved = consentRepository.save(consent);
        return toResponse(saved);
    }

    public List<ConsentResponse> listConsents(Long userId) {
        return consentRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ConsentResponse getConsent(Long id, Long userId) {
        Consent consent = getOwnedConsentOrThrow(id, userId);
        return toResponse(consent);
    }

    @Transactional
    public ConsentResponse revoke(Long id, Long userId) {
        Consent consent = getOwnedConsentOrThrow(id, userId);

        if (!"ACTIVE".equals(consent.getStatus())) {
            throw new ApiException("Only active consents can be revoked", HttpStatus.BAD_REQUEST);
        }

        consent.setStatus("REVOKED");
        consent.setRevokedAt(LocalDateTime.now());
        return toResponse(consentRepository.save(consent));
    }

    @Transactional
    public ConsentResponse renew(Long id, Long userId, int validityDays) {
        Consent consent = getOwnedConsentOrThrow(id, userId);

        if ("REVOKED".equals(consent.getStatus())) {
            throw new ApiException("Revoked consents cannot be renewed. Create a new consent instead.", HttpStatus.BAD_REQUEST);
        }

        LocalDateTime now = LocalDateTime.now();
        consent.setStatus("ACTIVE");
        consent.setIssuedAt(now);
        consent.setExpiresAt(now.plusDays(validityDays));
        return toResponse(consentRepository.save(consent));
    }

    // Called by banking-core-service (Day 9) before showing balance/transactions.
    // Note: no userId check here deliberately — this is an internal service-to-service
    // check keyed purely on the account, since banking-core-service already knows
    // which user owns the account from its own DB.
    public ConsentValidationResponse validate(Long linkedAccountId) {
        var consentOpt = consentRepository.findFirstByLinkedAccountIdAndStatusOrderByIssuedAtDesc(
                linkedAccountId, "ACTIVE");

        if (consentOpt.isEmpty()) {
            return ConsentValidationResponse.builder().valid(false).reason("NOT_FOUND").build();
        }

        Consent consent = consentOpt.get();

        if (consent.getExpiresAt().isBefore(LocalDateTime.now())) {
            consent.setStatus("EXPIRED");
            consentRepository.save(consent);
            return ConsentValidationResponse.builder().valid(false).reason("EXPIRED").build();
        }

        return ConsentValidationResponse.builder().valid(true).build();
    }

    private Consent getOwnedConsentOrThrow(Long id, Long userId) {
        return consentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException("Consent not found or access denied", HttpStatus.NOT_FOUND));
    }

    private ConsentResponse toResponse(Consent c) {
        return ConsentResponse.builder()
                .id(c.getId())
                .userId(c.getUserId())
                .linkedAccountId(c.getLinkedAccountId())
                .purpose(c.getPurpose())
                .dataScope(c.getDataScope())
                .status(c.getStatus())
                .issuedAt(c.getIssuedAt())
                .expiresAt(c.getExpiresAt())
                .revokedAt(c.getRevokedAt())
                .build();
    }
}