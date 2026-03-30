package com.dd25.dietiestates25.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record FullListingResponse(
    String name,
    String agentEmail,
    String agentName,
    BigDecimal price,
    String listingType,
    String formattedAddress,
    String intern,
    Integer floor,
    Boolean elevator,
    Integer squareMeters,
    Integer numberOfRooms,
    String energyClass,
    String otherServices,
    String description,
    Boolean nearStops,
    Boolean nearParks,
    Boolean nearSchools,
    List<String> imageUrls,
    List<String> imageDescriptions
) {}
