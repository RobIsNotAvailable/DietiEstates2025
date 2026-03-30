package com.dd25.dietiestates25.dto.request;

import com.dd25.dietiestates25.util.StringConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TokenLoginRequest(
    @NotBlank(message = StringConstants.NEW_PASSWORD_REQUIRED_MESSAGE)
    @Pattern
    (
        regexp = StringConstants.VALIDATION_PATTERN,
        message = StringConstants.PASSWORD_MESSAGE
    )
    String newPassword
) {}