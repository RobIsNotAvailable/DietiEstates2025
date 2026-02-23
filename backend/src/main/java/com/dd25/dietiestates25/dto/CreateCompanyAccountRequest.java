package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

import com.dd25.dietiestates25.model.enums.SecurityLevel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCompanyAccountRequest(
    @NonNull
    @NotBlank (message = "Email field cannot be blank")
    @Email (message = "Invalid email format")
    String email,

    @NotBlank (message = "First name field cannot be blank")
    String firstName,

    @NotBlank (message = "Last name field cannot be blank")
    String lastName,

    @NotNull (message = "Security level cannot be null")
    SecurityLevel securityLevel
) {}
