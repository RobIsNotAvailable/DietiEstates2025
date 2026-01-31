package com.dd25.dietiestates25.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountRegisterRequest(
    @NotBlank (message = "First name field cannot be blank")
    String firstName,

    @NotBlank (message = "Last name field cannot be blank")
    String lastName,

    @NotBlank (message = "Password field cannot be blank")
    String rawPassword
) {}