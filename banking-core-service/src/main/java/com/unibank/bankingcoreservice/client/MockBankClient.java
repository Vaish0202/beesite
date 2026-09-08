package com.unibank.bankingcoreservice.client;


import com.unibank.bankingcoreservice.client.dto.*;
import com.unibank.bankingcoreservice.exception.ApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class MockBankClient {

    private final RestClient restClient;

    public MockBankClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public List<ExternalAccountDto> getAccounts(String baseUrl, String bankCode, String customerRef) {
        String url = baseUrl + buildPath(bankCode, "/accounts/" + customerRef);
        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ExternalAccountDto>>() {});
        } catch (RestClientResponseException e) {
            throw new ApiException("Mock bank error: " + e.getStatusText(), HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            throw new ApiException("Unable to reach bank server", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ExternalBalanceDto getBalance(String baseUrl, String bankCode, String externalAccountId) {
        String url = baseUrl + buildPath(bankCode, "/accounts/" + externalAccountId + "/balance");
        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(ExternalBalanceDto.class);
        } catch (RestClientResponseException e) {
            throw new ApiException("Mock bank error: " + e.getStatusText(), HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            throw new ApiException("Unable to reach bank server", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public List<ExternalTransactionDto> getTransactions(String baseUrl, String bankCode, String externalAccountId) {
        String url = baseUrl + buildPath(bankCode, "/accounts/" + externalAccountId + "/transactions");
        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ExternalTransactionDto>>() {});
        } catch (RestClientResponseException e) {
            throw new ApiException("Mock bank error: " + e.getStatusText(), HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            throw new ApiException("Unable to reach bank server", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    // Builds a path like /mock/sbi/accounts/... generically from the bank code,
    // so this same client works for HDFC and ICICI once we add them on Day 15-16,
    // as long as those mock services follow the same /mock/{code}/... convention.
    private String buildPath(String bankCode, String suffix) {
        return "/mock/" + bankCode.toLowerCase() + suffix;
    }
}
