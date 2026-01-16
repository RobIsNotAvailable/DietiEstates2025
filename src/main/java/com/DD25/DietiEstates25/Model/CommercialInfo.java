package com.DD25.DietiEstates25.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "commercial_info")
public class CommercialInfo 
{
    @Id
    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "area", nullable = false)
    private Integer area;

    // Constructors

    protected CommercialInfo() {}

    public CommercialInfo(Integer listingId, String type, Integer area) 
    {
        this.listingId = listingId;
        this.type = type;
        this.area = area;
    }

    // Getters
    public Integer getListingId() { return listingId; }
    public String getType() { return type; }
    public Integer getArea() { return area; }

    // Setters
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setType(String type) { this.type = type; }
    public void setArea(Integer area) { this.area = area; }
}