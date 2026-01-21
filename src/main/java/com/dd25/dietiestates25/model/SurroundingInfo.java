package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "surrounding_info")
public class SurroundingInfo 
{
    @Id
    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "has_stops", nullable = false)
    private boolean hasStops;

    @Column(name = "has_parks", nullable = false)
    private boolean hasParks;

    @Column(name = "has_schools", nullable = false)
    private boolean hasSchools;

    // Constructors

    protected SurroundingInfo() {}


    public SurroundingInfo(Integer listingId, boolean hasStops, boolean hasParks, boolean hasSchools) 
    {
        this.listingId = listingId;
        this.hasStops = hasStops;
        this.hasParks = hasParks;
        this.hasSchools = hasSchools;
    }

    // Getters
    public Integer getListingId() { return listingId; }
    public boolean isHasStops() { return hasStops; }
    public boolean isHasParks() { return hasParks; }
    public boolean isHasSchools() { return hasSchools; }

    // Setters
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setHasStops(boolean hasStops) { this.hasStops = hasStops; }
    public void setHasParks(boolean hasParks) { this.hasParks = hasParks; }
    public void setHasSchools(boolean hasSchools) { this.hasSchools = hasSchools; }
}
