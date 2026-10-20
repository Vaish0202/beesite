package com.unibank.bankingcoreservice.repository;

import com.unibank.bankingcoreservice.entity.FdClosureRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FdClosureRequestRepository extends JpaRepository<FdClosureRequest, Long> {
    List<FdClosureRequest> findByUserId(Long userId);
    Optional<FdClosureRequest> findByIdAndUserId(Long id, Long userId);
}
