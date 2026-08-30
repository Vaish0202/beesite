package com.unibank.mocksbi.repository;

import com.unibank.mocksbi.entity.MockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockTransactionRepository extends JpaRepository<MockTransaction, Long> {
    List<MockTransaction> findByAccountIdOrderByTransactionTimeDesc(Long accountId);
}