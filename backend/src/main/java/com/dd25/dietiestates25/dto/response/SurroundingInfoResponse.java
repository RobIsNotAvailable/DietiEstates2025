package com.dd25.dietiestates25.dto.response;

public record SurroundingInfoResponse(
    boolean hasBus,
    boolean hasPark,
    boolean hasSchool
) {}