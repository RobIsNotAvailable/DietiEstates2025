package com.dd25.dietiestates25.dto;

import java.math.BigDecimal;

import com.dd25.dietiestates25.model.Address;
import com.dd25.dietiestates25.model.enums.ListingType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record CreateListingRequest(
    BigDecimal price,

    ListingType listingType,
    
    @NotBlank(message = "Description cannot be blank")
    String description,
    
    @NotBlank(message = "Address cannot be blank")
    Address address,
    
    @Min(value = 1, message = "Intern must be at least 1")
    Integer intern,
    
    Integer floor,
    
    Boolean hasElevator,
    
    @Min(value = 1, message = "Square meters must be at least 1")
    Integer squareMeters,
    
    @Min(value = 1, message = "Number of rooms must be at least 1")
    Integer numberOfRooms,
    
    @NotBlank(message = "Energy class cannot be blank")
    String energyClass,

    String otherServices
) {}
