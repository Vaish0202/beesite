package com.unibank.mocksbi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mock_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType; // CREDIT, DEBIT

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    @Column(nullable = false)
    @Builder.Default
    private String status = "SUCCESS";
}