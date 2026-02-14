package com.dd25.dietiestates25.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
    @NotBlank(message = "Old password field is required")
    @Pattern
    (
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,32}$", 
        message = "Password length must be between 8 and 32 characters long and contain both letters and numbers"

    )
    String oldPassword,

    @NotBlank(message = "New password field is required")
    @Pattern
    (
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,32}$", 
        message = "Password length must be between 8 and 32 characters long and contain both letters and numbers"

    )
    String newPassword
) {}