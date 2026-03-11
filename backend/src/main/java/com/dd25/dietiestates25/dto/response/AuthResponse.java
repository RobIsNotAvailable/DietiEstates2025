package com.dd25.dietiestates25.dto.response;

import org.springframework.lang.NonNull;

public record AuthResponse(
    @NonNull
    String token
) {}