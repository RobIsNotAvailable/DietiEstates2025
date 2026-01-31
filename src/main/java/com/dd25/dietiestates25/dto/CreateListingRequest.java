package com.dd25.dietiestates25.dto;

import java.math.BigDecimal;

import com.dd25.dietiestates25.model.enums.ListingType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record CreateListingRequest(
    @NotBlank(message = "Price field cannot be blank")
    BigDecimal price,

    @NotBlank(message = "Listing type cannot be blank")
    ListingType listingType,
    
    @NotBlank(message = "Description cannot be blank")
    String description,
    
    @NotBlank(message = "Address cannot be blank")
    String address,
    
    @NotBlank(message = "Intern cannot be blank")
    @Min(value = 1, message = "Intern must be at least 1")
    Integer intern,
    
    @NotBlank(message = "Floor cannot be blank")
    Integer floor,
    
    @NotBlank(message = "Elevator information cannot be blank")
    Boolean hasElevator,
    
    @NotBlank(message = "Square meters cannot be blank")
    @Min(value = 1, message = "Square meters must be at least 1")
    Integer squareMeters,
    
    @NotBlank(message = "Number of rooms cannot be blank")
    @Min(value = 1, message = "Number of rooms must be at least 1")
    Integer numberOfRooms,
    
    @NotBlank(message = "Energy class cannot be blank")
    String energyClass,

    String otherServices
) {}
