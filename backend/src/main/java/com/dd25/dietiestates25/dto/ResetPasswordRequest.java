package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

import com.dd25.dietiestates25.util.StringConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
    @NonNull
    @NotBlank (message = StringConstants.EMAIL_REQUIRED_MESSAGE)
    @Pattern
    (
        regexp = StringConstants.EMAIL_REGEX,
        message = StringConstants.INVALID_EMAIL_MESSAGE
    )
    String email
)
{ public String email() { return email.trim().toLowerCase(); }}
