package com.unibank.consent.controller;

import com.unibank.consent.dto.*;
import com.unibank.consent.service.ConsentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consents")
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping
    public ResponseEntity<ConsentResponse> create(@Valid @RequestBody CreateConsentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consentService.createConsent(request));
    }

    @GetMapping
    public List<ConsentResponse> list(@RequestParam Long userId) {
        return consentService.listConsents(userId);
    }

    @GetMapping("/{id}")
    public ConsentResponse get(@PathVariable Long id, @RequestParam Long userId) {
        return consentService.getConsent(id, userId);
    }

    @PostMapping("/{id}/revoke")
    public ConsentResponse revoke(@PathVariable Long id, @RequestParam Long userId) {
        return consentService.revoke(id, userId);
    }

    @PostMapping("/{id}/renew")
    public ConsentResponse renew(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "90") int validityDays) {
        return consentService.renew(id, userId, validityDays);
    }

    // Internal endpoint — called by banking-core-service, not the frontend directly.
    @GetMapping("/validate")
    public ConsentValidationResponse validate(@RequestParam Long linkedAccountId) {
        return consentService.validate(linkedAccountId);
    }
}