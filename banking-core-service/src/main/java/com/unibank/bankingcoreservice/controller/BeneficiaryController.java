package com.unibank.bankingcoreservice.controller;

import com.unibank.bankingcoreservice.dto.AddBeneficiaryRequest;
import com.unibank.bankingcoreservice.dto.BeneficiaryResponse;
import com.unibank.bankingcoreservice.service.BeneficiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> add(
            @Valid @RequestBody AddBeneficiaryRequest request, @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryService.addBeneficiary(request, userId));
    }

    @GetMapping
    public List<BeneficiaryResponse> list(@RequestParam Long userId) {
        return beneficiaryService.listBeneficiaries(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long userId) {
        beneficiaryService.deleteBeneficiary(id, userId);
        return ResponseEntity.noContent().build();
    }
}
