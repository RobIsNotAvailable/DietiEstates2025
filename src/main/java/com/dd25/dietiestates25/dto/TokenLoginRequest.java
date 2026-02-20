package com.dd25.dietiestates25.dto;

import com.dd25.dietiestates25.util.ValidationConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TokenLoginRequest(
    @NotBlank(message = "New password field is required")
    @Pattern
    (
        regexp = ValidationConstants.PASSWORD_REGEX,
        message = ValidationConstants.PASSWORD_MESSAGE
    )
    String newPassword
) {}