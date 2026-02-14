package com.dd25.dietiestates25.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyResponse(
    List<GeoapifyFeature> features
)
{
    public GeoapifyResponse
    {
        if (features == null || features.isEmpty())
            throw new IllegalArgumentException("Invalid response from given address");
    }


    public List<GeoapifyProperties> properties()
    {
        return features.stream()
                .map(GeoapifyFeature::properties)
                .toList();
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GeoapifyFeature(GeoapifyProperties properties) {}
}