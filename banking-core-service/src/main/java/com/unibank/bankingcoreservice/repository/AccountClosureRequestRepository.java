package com.unibank.bankingcoreservice.repository;

import com.unibank.bankingcoreservice.entity.AccountClosureRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountClosureRequestRepository extends JpaRepository<AccountClosureRequest, Long> {
    List<AccountClosureRequest> findByUserId(Long userId);
    Optional<AccountClosureRequest> findByIdAndUserId(Long id, Long userId);
}

