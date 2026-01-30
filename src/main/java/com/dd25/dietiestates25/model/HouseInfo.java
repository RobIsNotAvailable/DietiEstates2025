package com.dd25.dietiestates25.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "house_info")
public class HouseInfo 
{
    @Id
    private Listing id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "description", nullable = false)
    private String description;

    @Embedded
    private BuildingDetails buildingDetails;

    @Embedded
    private HouseDetails houseDetails;


    // Constructors

    protected HouseInfo() {}

    public HouseInfo(Listing listing, String description, BuildingDetails buildingDetails, HouseDetails houseDetails)
    {
        this.listing = listing;
        this.description = description;
        this.buildingDetails = buildingDetails;
        this.houseDetails = houseDetails;
    }


    // Getters and Setters
    public Listing getListing() { return listing; }
    public String getDescription() { return description; }
    public BuildingDetails getBuildingDetails() { return buildingDetails; }
    public HouseDetails getHouseDetails() { return houseDetails; }

    public void setListing(Listing listing) { this.listing = listing; }
    public void setDescription(String description) { this.description = description; }
}
