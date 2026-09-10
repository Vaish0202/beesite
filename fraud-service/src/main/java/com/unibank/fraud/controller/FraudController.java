package com.unibank.fraud.controller;

import com.unibank.fraud.dto.FraudScoreRequest;
import com.unibank.fraud.dto.FraudScoreResponse;
import com.unibank.fraud.service.FraudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
public class FraudController {

    private final FraudService fraudService;

    @PostMapping("/score")
    public ResponseEntity<FraudScoreResponse> score(@Valid @RequestBody FraudScoreRequest request) {
        return ResponseEntity.ok(fraudService.score(request));
    }
}
