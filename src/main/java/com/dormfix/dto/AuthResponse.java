package com.dormfix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private String name;
    private String email;  // ← Make sure email is included
    private Long userId;
    private String hostelBlock;
    private String roomNumber;
    
    // Constructor for backward compatibility
    public AuthResponse(String token, String role, String name, String email, Long userId) {
        this.token = token;
        this.role = role;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.hostelBlock = null;
        this.roomNumber = null;
    }
}