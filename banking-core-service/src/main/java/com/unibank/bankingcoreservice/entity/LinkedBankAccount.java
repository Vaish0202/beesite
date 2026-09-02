package com.unibank.bankingcoreservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "linked_bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkedBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "bank_provider_id", nullable = false)
    private Long bankProviderId;

    @Column(name = "external_account_id", nullable = false)
    private String externalAccountId; // the account ID inside the mock bank's own DB

    @Column(name = "masked_account_number", nullable = false)
    private String maskedAccountNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "balance_snapshot")
    private BigDecimal balanceSnapshot;

    @Column(nullable = false)
    @Builder.Default
    private String currency = "INR";

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
