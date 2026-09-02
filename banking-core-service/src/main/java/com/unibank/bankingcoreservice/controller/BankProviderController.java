package com.unibank.bankingcoreservice.controller;

import com.unibank.bankingcoreservice.entity.BankProvider;
import com.unibank.bankingcoreservice.repository.BankProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Temporary read-only endpoint just to confirm today's setup works end-to-end.
// The real /api/banks endpoint (per your Day 7 plan) replaces this tomorrow.
@RestController
@RequestMapping("/internal/bank-providers")
@RequiredArgsConstructor
public class BankProviderController {

    private final BankProviderRepository bankProviderRepository;

    @GetMapping
    public List<BankProvider> list() {
        return bankProviderRepository.findAll();
    }
}
