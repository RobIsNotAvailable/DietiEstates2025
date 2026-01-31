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

    @Column(name = "has_stops", nullable = false)
    private boolean hasStops;

    @Column(name = "has_parks", nullable = false)
    private boolean hasParks;

    @Column(name = "has_schools", nullable = false)
    private boolean hasSchools;

    // Constructors

    protected SurroundingInfo() {}


    public SurroundingInfo(Listing listing, boolean hasStops, boolean hasParks, boolean hasSchools) 
    {
        this.listing = listing;
        this.hasStops = hasStops;
        this.hasParks = hasParks;
        this.hasSchools = hasSchools;
    }

    // Getters
    public Listing getListing() { return listing; }
    public boolean isHasStops() { return hasStops; }
    public boolean isHasParks() { return hasParks; }
    public boolean isHasSchools() { return hasSchools; }

    // Setters
    public void setListing(Listing listing) { this.listing = listing; }
    public void setHasStops(boolean hasStops) { this.hasStops = hasStops; }
    public void setHasParks(boolean hasParks) { this.hasParks = hasParks; }
    public void setHasSchools(boolean hasSchools) { this.hasSchools = hasSchools; }
}
