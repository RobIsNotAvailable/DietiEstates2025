package com.dd25.dietiestates25.dto;

import java.math.BigDecimal;

import com.dd25.dietiestates25.model.enums.ListingType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateListingRequest(
    @NotBlank(message = "Name is required")
    String name,
    
    @NotNull(message = "Price is required")
    BigDecimal price,

    @NotNull(message = "Listing type is required")
    ListingType listingType,
    
    @NotBlank(message = "Description is required")
    String description,
    
    @NotBlank(message = "Address is required")
    String rawAddress,
    
    @NotNull(message = "Intern number is required")
    @Min(value = 1, message = "Intern must be at least 1")
    Integer intern,
    
    @NotNull(message = "Floor is required")
    Integer floor,
    
    @NotNull(message = "Please specify if there is an elevator")
    Boolean hasElevator,
    
    @NotNull(message = "Square meters are required")
    @Min(value = 1, message = "Square meters must be at least 1")
    Integer squareMeters,
    
    @NotNull(message = "Number of rooms is required")
    @Min(value = 1, message = "Number of rooms must be at least 1")
    Integer numberOfRooms,
    
    @NotBlank(message = "Energy class is required")
    String energyClass,

    String otherServices
) {}

