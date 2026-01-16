package com.DD25.DietiEstates25.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sorrounding_info")
public class SorroundingInfo 
{
    @Id
    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "nearby_schools", nullable = false)
    private String nearbySchools;

    @Column(name = "public_transport", nullable = false)
    private String publicTransport;

    @Column(name = "shopping_centers", nullable = false)
    private String shoppingCenters;

    // Constructors

    protected SorroundingInfo() {}


    public SorroundingInfo(Integer listingId, String nearbySchools, String publicTransport, String shoppingCenters) 
    {
        this.listingId = listingId;
        this.nearbySchools = nearbySchools;
        this.publicTransport = publicTransport;
        this.shoppingCenters = shoppingCenters;
    }

    // Getters
    public Integer getListingId() { return listingId; }
    public String getNearbySchools() { return nearbySchools; }
    public String getPublicTransport() { return publicTransport; }
    public String getShoppingCenters() { return shoppingCenters; }

    // Setters
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setNearbySchools(String nearbySchools) { this.nearbySchools = nearbySchools; }
    public void setPublicTransport(String publicTransport) { this.publicTransport = publicTransport; }
    public void setShoppingCenters(String shoppingCenters) { this.shoppingCenters = shoppingCenters; }    
}
