package com.dd25.dietiestates25.dto.response;

import java.math.BigDecimal;

public record SummaryListingResponse(
    Integer id,
    String name,
    BigDecimal price,
    String listingType,
    String formattedAddress,
    Integer squareMeters,
    String description,
    Boolean nearStops,
    Boolean nearParks,
    Boolean nearSchools,
    String imageUrl
) {}
