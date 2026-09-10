package com.unibank.bankingcoreservice.repository;

import com.unibank.bankingcoreservice.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByUserIdAndStatus(Long userId, String status);
    Optional<Beneficiary> findByIdAndUserId(Long id, Long userId);
}
