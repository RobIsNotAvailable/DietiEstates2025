package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.util.StringConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCompanyAccountRequest(
    @NonNull
    @NotBlank (message = StringConstants.EMAIL_REQUIRED_MESSAGE)
    @Email (message = StringConstants.INVALID_EMAIL_MESSAGE)
    String email,

    @NotBlank (message = StringConstants.FIRST_NAME_REQUIRED_MESSAGE)
    String firstName,

    @NotBlank (message = StringConstants.LAST_NAME_REQUIRED_MESSAGE)
    String lastName,

    @NotNull (message = "Security level cannot be null")
    SecurityLevel securityLevel
)
{ public String email() { return email.trim().toLowerCase(); }}
