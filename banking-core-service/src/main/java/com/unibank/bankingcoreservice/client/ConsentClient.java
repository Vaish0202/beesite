package com.unibank.bankingcoreservice.client;


import com.unibank.bankingcoreservice.client.dto.ConsentValidationDto;
import com.unibank.bankingcoreservice.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ConsentClient {

    private final RestClient restClient;
    private final String baseUrl;

    public ConsentClient(
            RestClient.Builder builder,
            @Value("${internal-services.consent-service.base-url}") String baseUrl
    ) {
        this.restClient = builder.build();
        this.baseUrl = baseUrl;
    }

    public ConsentValidationDto validate(Long linkedAccountId) {
        try {
            return restClient.get()
                    .uri(baseUrl + "/api/consents/validate?linkedAccountId=" + linkedAccountId)
                    .retrieve()
                    .body(ConsentValidationDto.class);
        } catch (RestClientResponseException e) {
            throw new ApiException("Consent service error: " + e.getStatusText(), HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            throw new ApiException("Unable to reach consent service", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}

