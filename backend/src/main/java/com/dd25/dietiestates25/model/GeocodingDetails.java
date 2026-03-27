package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class GeocodingDetails 
{
    @Column(name = "place_id", nullable = false)
    private String placeId;         

    @Column(name = "formatted_address", nullable = false)
    private String formattedAddress;

    public GeocodingDetails() {}

    public GeocodingDetails(String placeId, String formattedAddress) 
    {
        this.placeId = placeId;
        this.formattedAddress = formattedAddress;
    }

    public String getPlaceId() { return placeId; }
    public String getFormattedAddress() { return formattedAddress; }

    public void setPlaceId(String placeId) { this.placeId = placeId; }
    public void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress;}
}
