package com.unibank.auth.controller;

import com.unibank.auth.dto.*;
import com.unibank.auth.entity.User;
import com.unibank.auth.security.JwtService;
import com.unibank.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.validateLogin(request);
        String token = jwtService.generateToken(user);
        UserResponse userResponse = authService.getByEmail(user.getEmail());

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        String email = authentication.getName();
        UserResponse response = authService.getByEmail(email);
        return ResponseEntity.ok(response);
    }
}