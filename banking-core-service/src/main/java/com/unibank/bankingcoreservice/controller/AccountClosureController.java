package com.unibank.bankingcoreservice.controller;


import com.unibank.bankingcoreservice.dto.AccountClosureResponse;
import com.unibank.bankingcoreservice.dto.CreateAccountClosureRequest;
import com.unibank.bankingcoreservice.dto.ReviewClosureRequest;
import com.unibank.bankingcoreservice.service.AccountClosureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountClosureController {

    private final AccountClosureService accountClosureService;

    @PostMapping("/api/account-closures")
    public ResponseEntity<AccountClosureResponse> create(
            @Valid @RequestBody CreateAccountClosureRequest request, @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountClosureService.create(request, userId));
    }

    @GetMapping("/api/account-closures")
    public List<AccountClosureResponse> list(@RequestParam Long userId) {
        return accountClosureService.list(userId);
    }

    @GetMapping("/api/account-closures/{id}")
    public AccountClosureResponse get(@PathVariable Long id, @RequestParam Long userId) {
        return accountClosureService.get(id, userId);
    }

    @PostMapping("/api/admin/account-closures/{id}/approve")
    public AccountClosureResponse approve(@PathVariable Long id, @Valid @RequestBody ReviewClosureRequest request) {
        return accountClosureService.approve(id, request);
    }

    @PostMapping("/api/admin/account-closures/{id}/reject")
    public AccountClosureResponse reject(@PathVariable Long id, @Valid @RequestBody ReviewClosureRequest request) {
        return accountClosureService.reject(id, request);
    }
}