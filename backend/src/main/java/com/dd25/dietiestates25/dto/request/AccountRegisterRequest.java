package com.dd25.dietiestates25.dto.request;

import org.springframework.lang.NonNull;

import com.dd25.dietiestates25.util.StringConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AccountRegisterRequest(
    @NonNull
    @NotBlank (message = StringConstants.EMAIL_REQUIRED_MESSAGE)
    @Pattern
    (
        regexp = StringConstants.EMAIL_REGEX,
        message = StringConstants.INVALID_EMAIL_MESSAGE
    )
    String email,

    @NotBlank (message = StringConstants.FIRST_NAME_REQUIRED_MESSAGE)
    String firstName,

    @NotBlank (message = StringConstants.LAST_NAME_REQUIRED_MESSAGE)
    String lastName,

    @NotBlank (message = StringConstants.PASSWORD_REQUIRED_MESSAGE)
    @Pattern
    (
        regexp = StringConstants.VALIDATION_PATTERN,
        message = StringConstants.PASSWORD_MESSAGE
    )
    String rawPassword
) 
{ public String email() { return email.trim().toLowerCase(); }}