package com.unibank.bankingcoreservice.service;

import com.unibank.bankingcoreservice.dto.AddBeneficiaryRequest;
import com.unibank.bankingcoreservice.dto.BeneficiaryResponse;
import com.unibank.bankingcoreservice.entity.Beneficiary;
import com.unibank.bankingcoreservice.exception.ApiException;
import com.unibank.bankingcoreservice.repository.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryResponse addBeneficiary(AddBeneficiaryRequest request, Long userId) {
        String masked = maskAccountNumber(request.getAccountNumber());

        Beneficiary beneficiary = Beneficiary.builder()
                .userId(userId)
                .beneficiaryName(request.getBeneficiaryName())
                .bankName(request.getBankName())
                .accountNumberMasked(masked)
                .ifscCode(request.getIfscCode())
                .status("ACTIVE")
                .build();

        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    public List<BeneficiaryResponse> listBeneficiaries(Long userId) {
        return beneficiaryRepository.findByUserIdAndStatus(userId, "ACTIVE").stream()
                .map(this::toResponse)
                .toList();
    }

    // Soft delete — keeps the row for audit purposes rather than losing history
    public void deleteBeneficiary(Long id, Long userId) {
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException("Beneficiary not found or access denied", HttpStatus.NOT_FOUND));
        beneficiary.setStatus("REMOVED");
        beneficiaryRepository.save(beneficiary);
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return "XXXX";
        }
        String last4 = accountNumber.substring(accountNumber.length() - 4);
        return "X".repeat(accountNumber.length() - 4) + last4;
    }

    private BeneficiaryResponse toResponse(Beneficiary b) {
        return BeneficiaryResponse.builder()
                .id(b.getId())
                .beneficiaryName(b.getBeneficiaryName())
                .bankName(b.getBankName())
                .accountNumberMasked(b.getAccountNumberMasked())
                .ifscCode(b.getIfscCode())
                .status(b.getStatus())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
