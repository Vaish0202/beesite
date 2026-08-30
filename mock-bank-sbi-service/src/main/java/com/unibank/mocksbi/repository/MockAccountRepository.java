package com.unibank.mocksbi.repository;

import com.unibank.mocksbi.entity.MockAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockAccountRepository extends JpaRepository<MockAccount, Long> {
    List<MockAccount> findByCustomerRef(String customerRef);
}
