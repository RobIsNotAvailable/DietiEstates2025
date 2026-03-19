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
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Embedded
    private BuildingDetails buildingDetails;

    @Embedded
    private HouseDetails houseDetails;


    // Constructors

    protected HouseInfo() {}

    public HouseInfo(String description, BuildingDetails buildingDetails, HouseDetails houseDetails)
    {
        this.description = description;
        this.buildingDetails = buildingDetails;
        this.houseDetails = houseDetails;
    }

    // Getters
    public Listing getListing() { return listing; }
    public String getDescription() { return description; }
    public BuildingDetails getBuildingDetails() { return buildingDetails; }
    public HouseDetails getHouseDetails() { return houseDetails; }
    public String getCity() {return buildingDetails.getCity(); }
    public Integer getIntern() { return buildingDetails.getIntern(); }

    // Setters
    public void setListing(Listing listing) { this.listing = listing; }
    public void setDescription(String description) { this.description = description; }
}
