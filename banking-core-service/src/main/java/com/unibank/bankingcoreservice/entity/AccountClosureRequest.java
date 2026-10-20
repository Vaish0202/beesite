package com.unibank.bankingcoreservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_closure_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountClosureRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "linked_account_id", nullable = false)
    private Long linkedAccountId;

    private String reason;

    @Column(name = "transfer_balance_to")
    private String transferBalanceTo; // masked account number or beneficiary ref — free text for MVP

    @Column(nullable = false)
    @Builder.Default
    private String status = "SUBMITTED";

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
