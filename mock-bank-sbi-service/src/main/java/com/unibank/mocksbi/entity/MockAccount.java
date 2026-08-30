package com.unibank.mocksbi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "mock_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_ref", nullable = false)
    private String customerRef;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "masked_account_number", nullable = false)
    private String maskedAccountNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType; // SAVINGS, CURRENT

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    @Builder.Default
    private String currency = "INR";

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE";
}
