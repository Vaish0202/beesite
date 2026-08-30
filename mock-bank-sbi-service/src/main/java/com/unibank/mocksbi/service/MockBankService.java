package com.unibank.mocksbi.service;

import com.unibank.mocksbi.dto.*;
import com.unibank.mocksbi.entity.MockAccount;
import com.unibank.mocksbi.entity.MockTransaction;
import com.unibank.mocksbi.exception.ApiException;
import com.unibank.mocksbi.repository.MockAccountRepository;
import com.unibank.mocksbi.repository.MockTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MockBankService {

    private final MockAccountRepository accountRepository;
    private final MockTransactionRepository transactionRepository;

    public List<AccountResponse> getAccountsByCustomerRef(String customerRef) {
        List<MockAccount> accounts = accountRepository.findByCustomerRef(customerRef);
        if (accounts.isEmpty()) {
            throw new ApiException("No accounts found for customerRef: " + customerRef, HttpStatus.NOT_FOUND);
        }
        return accounts.stream().map(this::toAccountResponse).toList();
    }

    public BalanceResponse getBalance(Long accountId) {
        MockAccount account = getAccountOrThrow(accountId);
        return BalanceResponse.builder()
                .accountId(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .build();
    }

    public List<TransactionResponse> getTransactions(Long accountId) {
        getAccountOrThrow(accountId); // validates account exists
        return transactionRepository.findByAccountIdOrderByTransactionTimeDesc(accountId)
                .stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    @Transactional
    public SimulatePaymentResponse simulatePayment(SimulatePaymentRequest request) {
        MockAccount account = getAccountOrThrow(request.getAccountId());

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ApiException("Insufficient balance", HttpStatus.BAD_REQUEST);
        }

        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        MockTransaction transaction = MockTransaction.builder()
                .accountId(account.getId())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionTime(LocalDateTime.now())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);

        return SimulatePaymentResponse.builder()
                .transactionId(transaction.getId())
                .status("SUCCESS")
                .newBalance(newBalance)
                .build();
    }

    private MockAccount getAccountOrThrow(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ApiException("Account not found: " + accountId, HttpStatus.NOT_FOUND));
    }

    private AccountResponse toAccountResponse(MockAccount account) {
        return AccountResponse.builder()
                .accountId(account.getId())
                .customerRef(account.getCustomerRef())
                .maskedAccountNumber(account.getMaskedAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .build();
    }

    private TransactionResponse toTransactionResponse(MockTransaction tx) {
        return TransactionResponse.builder()
                .id(tx.getId())
                .transactionType(tx.getTransactionType())
                .amount(tx.getAmount())
                .description(tx.getDescription())
                .transactionTime(tx.getTransactionTime())
                .status(tx.getStatus())
                .build();
    }
}
