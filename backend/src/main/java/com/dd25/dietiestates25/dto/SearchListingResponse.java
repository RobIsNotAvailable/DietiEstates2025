package com.dd25.dietiestates25.dto;

import java.math.BigDecimal;
import java.util.List;

public record SearchListingResponse(
    String name,
    String agentEmail,
    String agentName,
    BigDecimal price,
    String listingType,
    String formattedAddress,
    Integer intern,
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
    List<String> imageUrls
) {}
