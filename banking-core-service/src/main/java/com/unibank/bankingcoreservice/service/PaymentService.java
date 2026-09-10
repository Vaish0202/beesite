package com.unibank.bankingcoreservice.service;

import com.unibank.bankingcoreservice.client.ConsentClient;
import com.unibank.bankingcoreservice.client.FraudClient;
import com.unibank.bankingcoreservice.client.MockBankClient;
import com.unibank.bankingcoreservice.client.dto.*;
import com.unibank.bankingcoreservice.dto.*;
import com.unibank.bankingcoreservice.entity.Beneficiary;
import com.unibank.bankingcoreservice.entity.BankProvider;
import com.unibank.bankingcoreservice.entity.LinkedBankAccount;
import com.unibank.bankingcoreservice.entity.PaymentRequest;
import com.unibank.bankingcoreservice.entity.Transaction;
import com.unibank.bankingcoreservice.exception.ApiException;
import com.unibank.bankingcoreservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final String SIMULATED_OTP = "123456";
    private static final int NEW_BENEFICIARY_WINDOW_MINUTES = 10;

    private final PaymentRequestRepository paymentRequestRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final LinkedBankAccountRepository linkedBankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final BankProviderRepository bankProviderRepository;
    private final MockBankClient mockBankClient;
    private final ConsentClient consentClient;
    private final FraudClient fraudClient;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request, Long userId) {
        LinkedBankAccount account = linkedBankAccountRepository.findByIdAndUserId(request.getFromAccountId(), userId)
                .orElseThrow(() -> new ApiException("Account not found or access denied", HttpStatus.NOT_FOUND));

        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUserId(request.getBeneficiaryId(), userId)
                .orElseThrow(() -> new ApiException("Beneficiary not found or access denied", HttpStatus.NOT_FOUND));

        enforceConsent(account.getId());

        // Save PENDING first so we have an ID to pass to fraud-service
        PaymentRequest payment = PaymentRequest.builder()
                .userId(userId)
                .fromAccountId(account.getId())
                .beneficiaryId(beneficiary.getId())
                .amount(request.getAmount())
                .remarks(request.getRemarks())
                .status("PENDING")
                .build();
        payment = paymentRequestRepository.save(payment);

        boolean newBeneficiary = beneficiary.getCreatedAt()
                .isAfter(LocalDateTime.now().minusMinutes(NEW_BENEFICIARY_WINDOW_MINUTES));
        long velocity = transactionRepository.countRecentByUserId(userId, LocalDateTime.now().minusHours(24));
        BigDecimal avgAmount = transactionRepository.averageAmountByUserId(userId);

        FraudScoreRequestDto fraudRequest = new FraudScoreRequestDto();
        fraudRequest.setUserId(userId);
        fraudRequest.setPaymentRequestId(payment.getId());
        fraudRequest.setAmount(request.getAmount());
        fraudRequest.setNewBeneficiary(newBeneficiary);
        fraudRequest.setDeviceChanged(Boolean.TRUE.equals(request.getDeviceChanged()));
        fraudRequest.setLocationChanged(Boolean.TRUE.equals(request.getLocationChanged()));
        fraudRequest.setFailedLoginCount(request.getFailedLoginCount() != null ? request.getFailedLoginCount() : 0);
        fraudRequest.setTransactionVelocity((int) velocity);
        fraudRequest.setAverageUserAmount(avgAmount);

        FraudScoreResponseDto fraudResult = fraudClient.score(fraudRequest);

        payment.setFraudScore(fraudResult.getScore());
        payment.setRiskLevel(fraudResult.getRiskLevel());

        switch (fraudResult.getRiskLevel()) {
            case "LOW" -> executePayment(payment, account, beneficiary);
            case "MEDIUM" -> payment.setStatus("MFA_REQUIRED");
            case "HIGH", "CRITICAL" -> payment.setStatus("HELD_FOR_REVIEW");
            default -> payment.setStatus("HELD_FOR_REVIEW");
        }

        PaymentRequest saved = paymentRequestRepository.save(payment);
        return toResponse(saved);
    }

    @Transactional
    public PaymentResponse confirmMfa(Long paymentId, Long userId, ConfirmMfaRequest request) {
        PaymentRequest payment = paymentRequestRepository.findByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new ApiException("Payment not found or access denied", HttpStatus.NOT_FOUND));

        if (!"MFA_REQUIRED".equals(payment.getStatus())) {
            throw new ApiException("This payment does not require MFA confirmation", HttpStatus.BAD_REQUEST);
        }

        if (!SIMULATED_OTP.equals(request.getOtp())) {
            throw new ApiException("Invalid OTP", HttpStatus.UNAUTHORIZED);
        }

        LinkedBankAccount account = linkedBankAccountRepository.findByIdAndUserId(payment.getFromAccountId(), userId)
                .orElseThrow(() -> new ApiException("Account not found", HttpStatus.NOT_FOUND));
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUserId(payment.getBeneficiaryId(), userId)
                .orElseThrow(() -> new ApiException("Beneficiary not found", HttpStatus.NOT_FOUND));

        executePayment(payment, account, beneficiary);
        PaymentRequest saved = paymentRequestRepository.save(payment);
        return toResponse(saved);
    }

    public List<PaymentResponse> listPayments(Long userId) {
        return paymentRequestRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public PaymentResponse getPayment(Long id, Long userId) {
        PaymentRequest payment = paymentRequestRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException("Payment not found or access denied", HttpStatus.NOT_FOUND));
        return toResponse(payment);
    }

    // Actually moves money via the mock bank and records a local transaction.
    // Sets FAILED (not an exception) if the mock bank rejects it (e.g.
    // insufficient balance) — a failed debit is a legitimate payment outcome,
    // not a system error.
    private void executePayment(PaymentRequest payment, LinkedBankAccount account, Beneficiary beneficiary) {
        BankProvider bank = bankProviderRepository.findById(account.getBankProviderId())
                .orElseThrow(() -> new ApiException("Bank provider not found", HttpStatus.INTERNAL_SERVER_ERROR));

        try {
            SimulatePaymentRequestDto simRequest = new SimulatePaymentRequestDto();
            simRequest.setAccountId(Long.valueOf(account.getExternalAccountId()));
            simRequest.setAmount(payment.getAmount());
            simRequest.setDescription("Payment to " + beneficiary.getBeneficiaryName());

            SimulatePaymentResponseDto simResponse = mockBankClient.simulatePayment(
                    bank.getApiBaseUrl(), bank.getBankCode(), simRequest);

            Transaction tx = Transaction.builder()
                    .linkedAccountId(account.getId())
                    .transactionType("DEBIT")
                    .amount(payment.getAmount())
                    .description("Payment to " + beneficiary.getBeneficiaryName())
                    .referenceNumber("TXN" + System.currentTimeMillis())
                    .status("SUCCESS")
                    .transactionTime(LocalDateTime.now())
                    .build();
            transactionRepository.save(tx);

            account.setBalanceSnapshot(simResponse.getNewBalance());
            linkedBankAccountRepository.save(account);

            payment.setStatus("SUCCESS");
        } catch (ApiException e) {
            payment.setStatus("FAILED");
        }
    }

    private void enforceConsent(Long linkedAccountId) {
        ConsentValidationDto validation = consentClient.validate(linkedAccountId);
        if (!validation.isValid()) {
            throw new ApiException(
                    "No active consent found for this account. Please provide consent first.",
                    HttpStatus.FORBIDDEN);
        }
    }

    private PaymentResponse toResponse(PaymentRequest p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .fromAccountId(p.getFromAccountId())
                .beneficiaryId(p.getBeneficiaryId())
                .amount(p.getAmount())
                .remarks(p.getRemarks())
                .status(p.getStatus())
                .fraudScore(p.getFraudScore())
                .riskLevel(p.getRiskLevel())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
