package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

import com.dd25.dietiestates25.util.StringConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
    @NonNull
    @NotBlank (message = StringConstants.EMAIL_REQUIRED_MESSAGE)
    @Email (message = StringConstants.INVALID_EMAIL_MESSAGE)
    String email,

    @NotBlank (message = StringConstants.PASSWORD_REQUIRED_MESSAGE)
    @Pattern
    (
        regexp = StringConstants.PASSWORD_REGEX,
        message = StringConstants.PASSWORD_MESSAGE
    )
    String rawPassword
) 
{ public String email() { return email.trim().toLowerCase(); }}