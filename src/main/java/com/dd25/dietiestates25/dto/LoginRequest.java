package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

import com.dd25.dietiestates25.util.ValidationConstants;

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
        regexp = ValidationConstants.PASSWORD_REGEX,
        message = ValidationConstants.PASSWORD_MESSAGE
    )
    String rawPassword
) {}