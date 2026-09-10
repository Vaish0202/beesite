package com.unibank.bankingcoreservice.controller;

import com.unibank.bankingcoreservice.dto.*;
import com.unibank.bankingcoreservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody CreatePaymentRequest request, @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request, userId));
    }

    @GetMapping
    public List<PaymentResponse> list(@RequestParam Long userId) {
        return paymentService.listPayments(userId);
    }

    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable Long id, @RequestParam Long userId) {
        return paymentService.getPayment(id, userId);
    }

    @PostMapping("/{id}/confirm-mfa")
    public PaymentResponse confirmMfa(
            @PathVariable Long id, @RequestParam Long userId, @Valid @RequestBody ConfirmMfaRequest request) {
        return paymentService.confirmMfa(id, userId, request);
    }
}
