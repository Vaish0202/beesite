package com.unibank.bankingcoreservice.service;

import com.unibank.bankingcoreservice.client.MockBankClient;
import com.unibank.bankingcoreservice.client.dto.*;
import com.unibank.bankingcoreservice.dto.*;
import com.unibank.bankingcoreservice.entity.BankProvider;
import com.unibank.bankingcoreservice.entity.LinkedBankAccount;
import com.unibank.bankingcoreservice.exception.ApiException;
import com.unibank.bankingcoreservice.repository.BankProviderRepository;
import com.unibank.bankingcoreservice.repository.LinkedBankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final BankProviderRepository bankProviderRepository;
    private final LinkedBankAccountRepository linkedBankAccountRepository;
    private final MockBankClient mockBankClient;

    public List<BankResponse> listBanks() {
        return bankProviderRepository.findAll().stream()
                .filter(b -> "ACTIVE".equals(b.getStatus()))
                .map(b -> BankResponse.builder()
                        .id(b.getId())
                        .name(b.getName())
                        .bankCode(b.getBankCode())
                        .status(b.getStatus())
                        .build())
                .toList();
    }

    public LinkedAccountResponse linkAccount(LinkAccountRequest request) {
        BankProvider bank = bankProviderRepository.findByBankCode(request.getBankCode())
                .orElseThrow(() -> new ApiException("Unknown bank code: " + request.getBankCode(), HttpStatus.NOT_FOUND));

        List<ExternalAccountDto> externalAccounts = mockBankClient.getAccounts(
                bank.getApiBaseUrl(), bank.getBankCode(), request.getCustomerRef());

        if (externalAccounts.isEmpty()) {
            throw new ApiException("No accounts found at bank for customerRef: " + request.getCustomerRef(), HttpStatus.NOT_FOUND);
        }

        // MVP: link the first account returned for this customerRef.
        // Letting the user pick from multiple accounts is a frontend concern
        // we'll add once the React UI exists.
        ExternalAccountDto external = externalAccounts.get(0);
        String externalAccountId = String.valueOf(external.getAccountId());

        if (linkedBankAccountRepository.existsByUserIdAndBankProviderIdAndExternalAccountId(
                request.getUserId(), bank.getId(), externalAccountId)) {
            throw new ApiException("This account is already linked", HttpStatus.CONFLICT);
        }

        LinkedBankAccount linked = LinkedBankAccount.builder()
                .userId(request.getUserId())
                .bankProviderId(bank.getId())
                .externalAccountId(externalAccountId)
                .maskedAccountNumber(external.getMaskedAccountNumber())
                .accountType(external.getAccountType())
                .balanceSnapshot(external.getBalance())
                .currency(external.getCurrency())
                .status("ACTIVE")
                .build();

        LinkedBankAccount saved = linkedBankAccountRepository.save(linked);
        return toResponse(saved, bank);
    }

    public List<LinkedAccountResponse> listAccounts(Long userId) {
        return linkedBankAccountRepository.findByUserId(userId).stream()
                .map(acc -> toResponse(acc, getBank(acc.getBankProviderId())))
                .toList();
    }

    public LinkedAccountResponse getAccount(Long id, Long userId) {
        LinkedBankAccount account = getOwnedAccountOrThrow(id, userId);
        return toResponse(account, getBank(account.getBankProviderId()));
    }

    public AccountBalanceResponse getBalance(Long id, Long userId) {
        LinkedBankAccount account = getOwnedAccountOrThrow(id, userId);
        BankProvider bank = getBank(account.getBankProviderId());

        ExternalBalanceDto liveBalance = mockBankClient.getBalance(
                bank.getApiBaseUrl(), bank.getBankCode(), account.getExternalAccountId());

        // Refresh the local snapshot whenever we do a live read, so dashboard
        // list views (which use the snapshot) don't go stale for too long.
        account.setBalanceSnapshot(liveBalance.getBalance());
        linkedBankAccountRepository.save(account);

        return AccountBalanceResponse.builder()
                .accountId(account.getId())
                .balance(liveBalance.getBalance())
                .currency(liveBalance.getCurrency())
                .build();
    }

    public List<AccountTransactionResponse> getTransactions(Long id, Long userId) {
        LinkedBankAccount account = getOwnedAccountOrThrow(id, userId);
        BankProvider bank = getBank(account.getBankProviderId());

        return mockBankClient.getTransactions(
                        bank.getApiBaseUrl(), bank.getBankCode(), account.getExternalAccountId())
                .stream()
                .map(tx -> AccountTransactionResponse.builder()
                        .id(tx.getId())
                        .transactionType(tx.getTransactionType())
                        .amount(tx.getAmount())
                        .description(tx.getDescription())
                        .transactionTime(tx.getTransactionTime())
                        .status(tx.getStatus())
                        .build())
                .toList();
    }

    private LinkedBankAccount getOwnedAccountOrThrow(Long id, Long userId) {
        return linkedBankAccountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException("Account not found or access denied", HttpStatus.NOT_FOUND));
    }

    private BankProvider getBank(Long bankProviderId) {
        return bankProviderRepository.findById(bankProviderId)
                .orElseThrow(() -> new ApiException("Bank provider not found", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private LinkedAccountResponse toResponse(LinkedBankAccount acc, BankProvider bank) {
        return LinkedAccountResponse.builder()
                .id(acc.getId())
                .bankCode(bank.getBankCode())
                .bankName(bank.getName())
                .maskedAccountNumber(acc.getMaskedAccountNumber())
                .accountType(acc.getAccountType())
                .balanceSnapshot(acc.getBalanceSnapshot())
                .currency(acc.getCurrency())
                .status(acc.getStatus())
                .createdAt(acc.getCreatedAt())
                .build();
    }
}