package com.dd25.dietiestates25.dto;

import java.math.BigDecimal;

import com.dd25.dietiestates25.model.enums.ListingType;

public record ListingSearchRequest
(
    String city,
    
    BigDecimal minPrice,
    
    BigDecimal maxPrice,

    Integer minRooms,

    String energyClass,

    ListingType listingType
) 
{}