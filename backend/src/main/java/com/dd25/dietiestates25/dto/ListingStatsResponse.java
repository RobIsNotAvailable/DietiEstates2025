package com.dd25.dietiestates25.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.dd25.dietiestates25.model.enums.Status;

public record ListingStatsResponse(
    Integer id,
    String name,
    BigDecimal price,
    String listingType,
    String formattedAddress,
    String imageUrl,

    Integer views,
    Integer visitsRecieved,
    Integer offersRecieved,
    BigDecimal highestOfferedPrice,
    OffsetDateTime lastModified,

    Status status,
    BigDecimal closurePrice
) {}

