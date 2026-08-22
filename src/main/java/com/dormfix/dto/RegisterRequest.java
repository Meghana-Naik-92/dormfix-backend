package com.dormfix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name must only contain letters and spaces")
    private String name;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$", 
             message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    private String role; // "STUDENT" or "ADMIN"

    private String hostelBlock;

    private String roomNumber;
}
