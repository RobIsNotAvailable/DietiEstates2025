package com.dd25.dietiestates25.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
    @NotBlank (message = "Old password field cannot be blank")
    String oldPassword,

    @NotBlank (message = "New password field cannot be blank")
    String newPassword
) {}