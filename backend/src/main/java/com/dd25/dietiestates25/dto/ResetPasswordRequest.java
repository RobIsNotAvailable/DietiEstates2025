package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
    @NonNull
    @NotBlank (message = "Email field is required")
    @Email (message = "Invalid email format")
    String email
) {}
