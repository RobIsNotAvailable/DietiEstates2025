package com.dd25.dietiestates25.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyProperties(
    String city,
    String street,

    @JsonProperty("housenumber")
    String houseNumber,

    String state,
    String postcode,
    String country,

    Double lat,
    Double lon,

    @JsonProperty("place_id")
    String placeId,

    String formatted,

    List<String> categories
){}
