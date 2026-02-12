package com.dd25.dietiestates25.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRegisterRequest
(

    @NotBlank (message = "Email field cannot be blank")
    @NotNull (message = "Email field cannot be null")
    @Email
    String email,

    @NotBlank (message = "First name field cannot be blank")
    @NotNull (message = "First name field cannot be null")
    String firstName,

    @NotBlank (message = "Last name field cannot be blank")
    @NotNull (message = "Last name field cannot be null")
    String lastName,

    @NotBlank (message = "Password field cannot be blank")
    @NotNull (message = "Password field cannot be null")
    String rawPassword
) {}