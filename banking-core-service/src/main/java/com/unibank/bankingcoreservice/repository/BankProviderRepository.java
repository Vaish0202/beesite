package com.unibank.bankingcoreservice.repository;

import com.unibank.bankingcoreservice.entity.BankProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankProviderRepository extends JpaRepository<BankProvider, Long> {
    Optional<BankProvider> findByBankCode(String bankCode);
}
