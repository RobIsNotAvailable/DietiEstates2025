package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NonNull
    @NotBlank (message = "Mail field cannot be blank")
    @Email (message = "Invalid email format")
    String email,

    @NotBlank (message = "Password field cannot be blank")
    String rawPassword
) {}