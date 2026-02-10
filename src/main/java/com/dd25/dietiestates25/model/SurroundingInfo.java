package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "surrounding_info")

public class SurroundingInfo 
{
    @Id
    private Integer id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "near_stops", nullable = false)
    private boolean nearStops;

    @Column(name = "near_parks", nullable = false)
    private boolean nearParks;

    @Column(name = "near_schools", nullable = false)
    private boolean nearSchools;

    // Constructors
    protected SurroundingInfo() {}

    public SurroundingInfo(boolean nearStops, boolean nearParks, boolean nearSchools) 
    {
        this.nearStops = nearStops;
        this.nearParks = nearParks;
        this.nearSchools = nearSchools;
    }

    // Getters
    public boolean isNearStops() { return nearStops; }
    public boolean isNearParks() { return nearParks; }
    public boolean isNearSchools() { return nearSchools; }

    // Setters
    public void setNearStops(boolean nearStops) { this.nearStops = nearStops; }
    public void setNearParks(boolean nearParks) { this.nearParks = nearParks; }
    public void setNearSchools(boolean nearSchools) { this.nearSchools = nearSchools; }
}
