package com.dd25.dietiestates25.dto.response;

public record AgentStatsResponse(
    Integer activeListings,
    Integer concludedListings,
    Integer nViews,
    Integer activeVisits,
    Integer concludedVisits,
    Integer nOffers
) {}