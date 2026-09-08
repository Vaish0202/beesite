package com.unibank.bankingcoreservice.controller;


import com.unibank.bankingcoreservice.dto.*;
import com.unibank.bankingcoreservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// NOTE: userId is a query param for now — this is temporary. Once api-gateway
// and JWT propagation exist (Day 17+), userId will come from the authenticated
// principal instead of being passed in by the caller.
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/link")
    public ResponseEntity<LinkedAccountResponse> linkAccount(@Valid @RequestBody LinkAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.linkAccount(request));
    }

    @GetMapping
    public List<LinkedAccountResponse> listAccounts(@RequestParam Long userId) {
        return accountService.listAccounts(userId);
    }

    @GetMapping("/{id}")
    public LinkedAccountResponse getAccount(@PathVariable Long id, @RequestParam Long userId) {
        return accountService.getAccount(id, userId);
    }

    @GetMapping("/{id}/balance")
    public AccountBalanceResponse getBalance(@PathVariable Long id, @RequestParam Long userId) {
        return accountService.getBalance(id, userId);
    }

    @GetMapping("/{id}/transactions")
    public List<AccountTransactionResponse> getTransactions(@PathVariable Long id, @RequestParam Long userId) {
        return accountService.getTransactions(id, userId);
    }
}