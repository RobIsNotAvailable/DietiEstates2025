package com.dd25.dietiestates25.dto;

import com.dd25.dietiestates25.util.StringConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
    @NotBlank(message = StringConstants.OLD_PASSWORD_REQUIRED_MESSAGE)
    @Pattern
    (
        regexp = StringConstants.PASSWORD_REGEX, 
        message = StringConstants.PASSWORD_MESSAGE
    )
    String oldPassword,

    @NotBlank(message = StringConstants.NEW_PASSWORD_REQUIRED_MESSAGE)
    @Pattern
    (
        regexp = StringConstants.PASSWORD_REGEX,
        message = StringConstants.PASSWORD_MESSAGE
    )
    String newPassword
) {}