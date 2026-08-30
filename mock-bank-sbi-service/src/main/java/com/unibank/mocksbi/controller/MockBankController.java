package com.unibank.mocksbi.controller;

import com.unibank.mocksbi.dto.*;
import com.unibank.mocksbi.service.MockBankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mock/sbi")
@RequiredArgsConstructor
public class MockBankController {

    private final MockBankService mockBankService;

    @GetMapping("/accounts/{customerRef}")
    public ResponseEntity<List<AccountResponse>> getAccounts(@PathVariable String customerRef) {
        return ResponseEntity.ok(mockBankService.getAccountsByCustomerRef(customerRef));
    }

    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(mockBankService.getBalance(accountId));
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(mockBankService.getTransactions(accountId));
    }

    @PostMapping("/payments/simulate")
    public ResponseEntity<SimulatePaymentResponse> simulatePayment(@Valid @RequestBody SimulatePaymentRequest request) {
        return ResponseEntity.ok(mockBankService.simulatePayment(request));
    }
}
