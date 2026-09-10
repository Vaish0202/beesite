package com.unibank.bankingcoreservice.repository;

import com.unibank.bankingcoreservice.entity.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {
    List<PaymentRequest> findByUserId(Long userId);
    Optional<PaymentRequest> findByIdAndUserId(Long id, Long userId);
}
