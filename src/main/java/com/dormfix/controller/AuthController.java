package com.dormfix.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dormfix.dto.ApiResponse;
import com.dormfix.dto.AuthResponse;
import com.dormfix.dto.ForgotPasswordRequest;
import com.dormfix.dto.LoginRequest;
import com.dormfix.dto.RegisterRequest;
import com.dormfix.dto.ResetPasswordRequest;
import com.dormfix.service.AuthService;
import com.dormfix.service.PasswordResetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final JavaMailSender mailSender;  // ← ADD THIS LINE

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

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.sendResetEmail(request.getEmail());
            return ResponseEntity.ok(new ApiResponse(true,
                    "Password reset email sent. Check your inbox."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse(true,
                    "Password reset successfully. You can now login with your new password."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("meghananaik477@gmail.com"); // Your email for testing
            message.setSubject("DormFix - Email Test");
            message.setText("✅ Your email configuration is working! This is a test email from DormFix.");
            mailSender.send(message);
            return ResponseEntity.ok("✅ Email sent successfully! Check your inbox.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Error: " + e.getMessage());
        }
    }
}
/*
What's happening:

@RequestMapping("/auth") — all routes start with /auth
@Valid — triggers the validation annotations in your DTOs (@NotBlank, @Email etc.)
@CrossOrigin(origins = "*") — allows your React frontend to call this API
ResponseEntity.ok(...) — returns HTTP 200 with the response body


 */
