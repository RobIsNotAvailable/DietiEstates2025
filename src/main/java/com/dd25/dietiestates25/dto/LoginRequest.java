package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
    @NonNull
    @NotBlank (message = "Email field is required")
    @Email (message = "Invalid email format")
    String email,

    @NotBlank (message = "Password field is required")
    @Pattern
    (
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,32}$", 
        message = "Password length must be between 8 and 32 characters long and contain both letters and numbers"
    )
    String rawPassword
) {}