package com.DD25.DietiEstates25.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "house_info")
public class HouseInfo 
{
    @Id
    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "description", nullable = false)
    private String description;

    @Embedded
    private LocationInfo locationInfo;

    @Embedded
    private HouseDetails houseDetails;


    // Constructors

    protected HouseInfo() {}

    public HouseInfo(Integer listingId, String description, LocationInfo locationInfo, HouseDetails houseDetails)
    {
        this.listingId = listingId;
        this.description = description;
        this.locationInfo = locationInfo;
        this.houseDetails = houseDetails;
    }


    // Getters and Setters
    public Integer getListingId() { return listingId; }
    public String getDescription() { return description; }
    public LocationInfo getLocationInfo() { return locationInfo; }
    public HouseDetails getHouseDetails() { return houseDetails; }

    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setDescription(String description) { this.description = description; }
}
