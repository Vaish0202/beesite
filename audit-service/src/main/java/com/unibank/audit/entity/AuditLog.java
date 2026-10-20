package com.unibank.audit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // nullable — some actions (e.g. failed login before auth) may not resolve to a known user

    @Column(nullable = false)
    private String action; // USER_REGISTERED, PAYMENT_HELD, etc. — see fixed list below

    @Column(name = "resource_type")
    private String resourceType; // e.g. "PAYMENT", "CONSENT", "ACCOUNT"

    @Column(name = "resource_id")
    private String resourceId; // stored as String since resource IDs come from different services/tables

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}