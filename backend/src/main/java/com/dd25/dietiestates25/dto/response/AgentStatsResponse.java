package com.dd25.dietiestates25.dto.response;

public record AgentStatsResponse(
    Integer activeListings,
    Integer concludedListings
) {}