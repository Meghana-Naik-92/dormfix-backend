package com.dormfix.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dormfix.dto.AuthResponse;
import com.dormfix.dto.LoginRequest;
import com.dormfix.dto.RegisterRequest;
import com.dormfix.entity.Role;
import com.dormfix.entity.User;
import com.dormfix.repository.UserRepository;
import com.dormfix.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        // 1. Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // 2. Determine role (default to STUDENT if not provided)
        Role role = Role.STUDENT;
        if (request.getRole() != null
                && request.getRole().equalsIgnoreCase("ADMIN")) {
            role = Role.ADMIN;
        }

        // 3. Build and save user (password gets hashed by BCrypt)
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .hostelBlock(request.getHostelBlock())
                .roomNumber(request.getRoomNumber())
                .build();

        userRepository.save(user);

        // 4. Generate and return JWT token with user details
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getName(),
                user.getEmail(), // ← ADD EMAIL HERE (4th parameter)
                user.getId(),
                user.getHostelBlock(),
                user.getRoomNumber()
        );
    }

    public AuthResponse login(LoginRequest request) {

        // 1. Authenticate — throws exception if wrong credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Load user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Generate and return JWT token with user details
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getName(),
                user.getEmail(), // ← ADD EMAIL HERE (4th parameter)
                user.getId(),
                user.getHostelBlock(),
                user.getRoomNumber()
        );
    }
}
/*
What's happening here:

register — validates email is unique, hashes the password with BCrypt, saves to DB, returns a token immediately (user is logged in right after registering)
login — authenticationManager.authenticate() does all the heavy lifting: loads user, compares BCrypt hash, throws BadCredentialsException if wrong
We never store plain text passwords — passwordEncoder.encode() turns "mypassword" into something like "$2a$10$..."
 */
