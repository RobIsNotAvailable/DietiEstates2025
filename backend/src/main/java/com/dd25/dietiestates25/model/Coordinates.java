package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class Coordinates 
{
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;
    
    public Coordinates() {}

    public Coordinates(Double latitude, Double longitude) 
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    
}
