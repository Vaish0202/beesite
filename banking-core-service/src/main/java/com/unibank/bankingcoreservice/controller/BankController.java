package com.unibank.bankingcoreservice.controller;

import com.unibank.bankingcoreservice.dto.BankResponse;
import com.unibank.bankingcoreservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {

    private final AccountService accountService;

    @GetMapping
    public List<BankResponse> listBanks() {
        return accountService.listBanks();
    }
}
