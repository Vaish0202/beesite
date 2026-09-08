package com.unibank.consent.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "linked_account_id", nullable = false)
    private Long linkedAccountId;

    @Column(nullable = false)
    private String purpose; // e.g. "ACCOUNT_AGGREGATION", "PAYMENT_INITIATION"

    @Column(name = "data_scope", nullable = false)
    private String dataScope; // e.g. "BALANCE_AND_TRANSACTIONS"

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, REVOKED, EXPIRED

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;
}