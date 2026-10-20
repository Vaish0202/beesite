package com.unibank.bankingcoreservice.service;

import com.unibank.bankingcoreservice.dto.CreateFdClosureRequest;
import com.unibank.bankingcoreservice.dto.FdClosureResponse;
import com.unibank.bankingcoreservice.dto.ReviewClosureRequest;
import com.unibank.bankingcoreservice.entity.FdClosureRequest;
import com.unibank.bankingcoreservice.entity.LinkedBankAccount;
import com.unibank.bankingcoreservice.exception.ApiException;
import com.unibank.bankingcoreservice.repository.FdClosureRequestRepository;
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
public class FdClosureService {

    private static final Set<String> REVIEWABLE_STATUSES = Set.of("SUBMITTED", "UNDER_REVIEW");

    private final FdClosureRequestRepository fdClosureRequestRepository;
    private final LinkedBankAccountRepository linkedBankAccountRepository;

    public FdClosureResponse create(CreateFdClosureRequest request, Long userId) {
        LinkedBankAccount account = linkedBankAccountRepository.findByIdAndUserId(request.getLinkedAccountId(), userId)
                .orElseThrow(() -> new ApiException("Account not found or access denied", HttpStatus.NOT_FOUND));

        FdClosureRequest fdClosure = FdClosureRequest.builder()
                .userId(userId)
                .linkedAccountId(account.getId())
                .fdNumberMasked(mask(request.getFdNumber()))
                .reason(request.getReason())
                .status("SUBMITTED")
                .build();

        return toResponse(fdClosureRequestRepository.save(fdClosure));
    }

    public List<FdClosureResponse> list(Long userId) {
        return fdClosureRequestRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public FdClosureResponse get(Long id, Long userId) {
        return toResponse(getOwnedOrThrow(id, userId));
    }

    @Transactional
    public FdClosureResponse approve(Long id, ReviewClosureRequest request) {
        FdClosureRequest fd = getByIdOrThrow(id);
        assertReviewable(fd.getStatus());
        fd.setStatus("APPROVED");
        fd.setReviewedBy(request.getReviewerId());
        fd.setReviewedAt(LocalDateTime.now());
        return toResponse(fdClosureRequestRepository.save(fd));
    }

    @Transactional
    public FdClosureResponse reject(Long id, ReviewClosureRequest request) {
        FdClosureRequest fd = getByIdOrThrow(id);
        assertReviewable(fd.getStatus());
        fd.setStatus("REJECTED");
        fd.setReviewedBy(request.getReviewerId());
        fd.setReviewedAt(LocalDateTime.now());
        return toResponse(fdClosureRequestRepository.save(fd));
    }

    private void assertReviewable(String status) {
        if (!REVIEWABLE_STATUSES.contains(status)) {
            throw new ApiException("Only SUBMITTED or UNDER_REVIEW requests can be reviewed", HttpStatus.BAD_REQUEST);
        }
    }

    private FdClosureRequest getOwnedOrThrow(Long id, Long userId) {
        return fdClosureRequestRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException("FD closure request not found or access denied", HttpStatus.NOT_FOUND));
    }

    // Admin lookups aren't scoped to a specific customer's userId, since the
    // reviewer is a different user (an admin/support officer) than the request
    // creator. Real role-based enforcement (ensuring the caller IS an admin)
    // comes with api-gateway + JWT role propagation in the later hardening phase.
    private FdClosureRequest getByIdOrThrow(Long id) {
        return fdClosureRequestRepository.findById(id)
                .orElseThrow(() -> new ApiException("FD closure request not found", HttpStatus.NOT_FOUND));
    }

    private String mask(String fdNumber) {
        if (fdNumber == null || fdNumber.length() < 4) return "XXXX";
        String last4 = fdNumber.substring(fdNumber.length() - 4);
        return "X".repeat(fdNumber.length() - 4) + last4;
    }

    private FdClosureResponse toResponse(FdClosureRequest fd) {
        return FdClosureResponse.builder()
                .id(fd.getId())
                .linkedAccountId(fd.getLinkedAccountId())
                .fdNumberMasked(fd.getFdNumberMasked())
                .reason(fd.getReason())
                .status(fd.getStatus())
                .reviewedBy(fd.getReviewedBy())
                .reviewedAt(fd.getReviewedAt())
                .createdAt(fd.getCreatedAt())
                .build();
    }
}