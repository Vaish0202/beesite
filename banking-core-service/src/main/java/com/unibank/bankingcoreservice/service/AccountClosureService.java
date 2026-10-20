package com.unibank.bankingcoreservice.service;

import com.unibank.bankingcoreservice.dto.AccountClosureResponse;
import com.unibank.bankingcoreservice.dto.CreateAccountClosureRequest;
import com.unibank.bankingcoreservice.dto.ReviewClosureRequest;
import com.unibank.bankingcoreservice.entity.AccountClosureRequest;
import com.unibank.bankingcoreservice.entity.LinkedBankAccount;
import com.unibank.bankingcoreservice.exception.ApiException;
import com.unibank.bankingcoreservice.repository.AccountClosureRequestRepository;
import com.unibank.bankingcoreservice.repository.LinkedBankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountClosureService {

    private static final Set<String> REVIEWABLE_STATUSES = Set.of("SUBMITTED", "UNDER_REVIEW");

    private final AccountClosureRequestRepository accountClosureRequestRepository;
    private final LinkedBankAccountRepository linkedBankAccountRepository;

    public AccountClosureResponse create(CreateAccountClosureRequest request, Long userId) {
        LinkedBankAccount account = linkedBankAccountRepository.findByIdAndUserId(request.getLinkedAccountId(), userId)
                .orElseThrow(() -> new ApiException("Account not found or access denied", HttpStatus.NOT_FOUND));

        AccountClosureRequest closure = AccountClosureRequest.builder()
                .userId(userId)
                .linkedAccountId(account.getId())
                .reason(request.getReason())
                .transferBalanceTo(request.getTransferBalanceTo())
                .status("SUBMITTED")
                .build();

        return toResponse(accountClosureRequestRepository.save(closure));
    }

    public List<AccountClosureResponse> list(Long userId) {
        return accountClosureRequestRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public AccountClosureResponse get(Long id, Long userId) {
        return toResponse(getOwnedOrThrow(id, userId));
    }

    @Transactional
    public AccountClosureResponse approve(Long id, ReviewClosureRequest request) {
        AccountClosureRequest closure = getByIdOrThrow(id);
        assertReviewable(closure.getStatus());
        closure.setStatus("APPROVED");
        closure.setReviewedBy(request.getReviewerId());
        closure.setReviewedAt(LocalDateTime.now());
        AccountClosureRequest saved = accountClosureRequestRepository.save(closure);

        // On approval, mark the underlying linked account as CLOSED so it
        // stops showing up as an active account elsewhere in the system.
        linkedBankAccountRepository.findById(closure.getLinkedAccountId()).ifPresent(acc -> {
            acc.setStatus("CLOSED");
            linkedBankAccountRepository.save(acc);
        });

        return toResponse(saved);
    }

    @Transactional
    public AccountClosureResponse reject(Long id, ReviewClosureRequest request) {
        AccountClosureRequest closure = getByIdOrThrow(id);
        assertReviewable(closure.getStatus());
        closure.setStatus("REJECTED");
        closure.setReviewedBy(request.getReviewerId());
        closure.setReviewedAt(LocalDateTime.now());
        return toResponse(accountClosureRequestRepository.save(closure));
    }

    private void assertReviewable(String status) {
        if (!REVIEWABLE_STATUSES.contains(status)) {
            throw new ApiException("Only SUBMITTED or UNDER_REVIEW requests can be reviewed", HttpStatus.BAD_REQUEST);
        }
    }

    private AccountClosureRequest getOwnedOrThrow(Long id, Long userId) {
        return accountClosureRequestRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException("Account closure request not found or access denied", HttpStatus.NOT_FOUND));
    }

    private AccountClosureRequest getByIdOrThrow(Long id) {
        return accountClosureRequestRepository.findById(id)
                .orElseThrow(() -> new ApiException("Account closure request not found", HttpStatus.NOT_FOUND));
    }

    private AccountClosureResponse toResponse(AccountClosureRequest c) {
        return AccountClosureResponse.builder()
                .id(c.getId())
                .linkedAccountId(c.getLinkedAccountId())
                .reason(c.getReason())
                .transferBalanceTo(c.getTransferBalanceTo())
                .status(c.getStatus())
                .reviewedBy(c.getReviewedBy())
                .reviewedAt(c.getReviewedAt())
                .createdAt(c.getCreatedAt())
                .build();
    }
}