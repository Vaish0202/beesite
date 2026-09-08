package com.unibank.bankingcoreservice.repository;

import com.unibank.bankingcoreservice.entity.LinkedBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LinkedBankAccountRepository extends JpaRepository<LinkedBankAccount, Long> {
    List<LinkedBankAccount> findByUserId(Long userId);
    Optional<LinkedBankAccount> findByIdAndUserId(Long id, Long userId);
    boolean existsByUserIdAndBankProviderIdAndExternalAccountId(Long userId, Long bankProviderId, String externalAccountId);
}
