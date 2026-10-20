package com.unibank.bankingcoreservice.controller;

import com.unibank.bankingcoreservice.dto.CreateFdClosureRequest;
import com.unibank.bankingcoreservice.dto.FdClosureResponse;
import com.unibank.bankingcoreservice.dto.ReviewClosureRequest;
import com.unibank.bankingcoreservice.service.FdClosureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FdClosureController {

    private final FdClosureService fdClosureService;

    @PostMapping("/api/fd-closures")
    public ResponseEntity<FdClosureResponse> create(
            @Valid @RequestBody CreateFdClosureRequest request, @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fdClosureService.create(request, userId));
    }

    @GetMapping("/api/fd-closures")
    public List<FdClosureResponse> list(@RequestParam Long userId) {
        return fdClosureService.list(userId);
    }

    @GetMapping("/api/fd-closures/{id}")
    public FdClosureResponse get(@PathVariable Long id, @RequestParam Long userId) {
        return fdClosureService.get(id, userId);
    }

    @PostMapping("/api/admin/fd-closures/{id}/approve")
    public FdClosureResponse approve(@PathVariable Long id, @Valid @RequestBody ReviewClosureRequest request) {
        return fdClosureService.approve(id, request);
    }

    @PostMapping("/api/admin/fd-closures/{id}/reject")
    public FdClosureResponse reject(@PathVariable Long id, @Valid @RequestBody ReviewClosureRequest request) {
        return fdClosureService.reject(id, request);
    }
}
