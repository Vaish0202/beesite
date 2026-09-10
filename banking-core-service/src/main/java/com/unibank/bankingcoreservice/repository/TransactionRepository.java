package com.unibank.bankingcoreservice.repository;

import com.unibank.bankingcoreservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Powers fraud-service's "transactionVelocity" input — how many payments
    // this user has made recently, joined through their linked accounts since
    // Transaction only stores linkedAccountId, not userId directly.
    @Query("""
        SELECT COUNT(t) FROM Transaction t
        JOIN LinkedBankAccount a ON a.id = t.linkedAccountId
        WHERE a.userId = :userId AND t.transactionTime >= :since
        """)
    long countRecentByUserId(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    // Powers fraud-service's "averageUserAmount" input.
    @Query("""
        SELECT COALESCE(AVG(t.amount), 0) FROM Transaction t
        JOIN LinkedBankAccount a ON a.id = t.linkedAccountId
        WHERE a.userId = :userId AND t.status = 'SUCCESS'
        """)
    BigDecimal averageAmountByUserId(@Param("userId") Long userId);
}
