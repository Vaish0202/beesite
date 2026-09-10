package com.unibank.bankingcoreservice.client;

import com.unibank.bankingcoreservice.client.dto.FraudScoreRequestDto;
import com.unibank.bankingcoreservice.client.dto.FraudScoreResponseDto;
import com.unibank.bankingcoreservice.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class FraudClient {

    private final RestClient restClient;
    private final String baseUrl;

    public FraudClient(
            RestClient.Builder builder,
            @Value("${internal-services.fraud-service.base-url}") String baseUrl
    ) {
        this.restClient = builder.build();
        this.baseUrl = baseUrl;
    }

    public FraudScoreResponseDto score(FraudScoreRequestDto request) {
        try {
            return restClient.post()
                    .uri(baseUrl + "/api/fraud/score")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(FraudScoreResponseDto.class);
        } catch (RestClientResponseException e) {
            throw new ApiException("Fraud service error: " + e.getStatusText(), HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            throw new ApiException("Unable to reach fraud service", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
