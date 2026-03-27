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
    private Long userId;
    private String hostelBlock;
    private String roomNumber;

    // Constructor without hostelBlock and roomNumber for backward compatibility
    public AuthResponse(String token, String role, String name, Long userId) {
        this.token = token;
        this.role = role;
        this.name = name;
        this.userId = userId;
        this.hostelBlock = null;
        this.roomNumber = null;
    }
}
