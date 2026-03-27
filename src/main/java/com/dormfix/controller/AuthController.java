package com.dormfix.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dormfix.dto.AuthResponse;
import com.dormfix.dto.LoginRequest;
import com.dormfix.dto.RegisterRequest;
import com.dormfix.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
/*
What's happening:

@RequestMapping("/auth") — all routes start with /auth
@Valid — triggers the validation annotations in your DTOs (@NotBlank, @Email etc.)
@CrossOrigin(origins = "*") — allows your React frontend to call this API
ResponseEntity.ok(...) — returns HTTP 200 with the response body


*/
