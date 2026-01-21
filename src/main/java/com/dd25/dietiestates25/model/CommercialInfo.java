package com.dd25.dietiestates25.model;

import java.math.BigDecimal;

import com.dd25.dietiestates25.model.Enums.ListingType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "commercial_info")
public class CommercialInfo 
{
    @Id
    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "listing_type", nullable = false)
    private ListingType listingType;

    // Constructors

    protected CommercialInfo() {}

    public CommercialInfo(Integer listingId, BigDecimal price, ListingType listingType) 
    {
        this.listingId = listingId;
        this.price = price;
        this.listingType = listingType;
    }

    // Getters
    public Integer getListingId() { return listingId; }
    public BigDecimal getPrice() { return price; }
    public ListingType getListingType() { return listingType; }

    // Setters
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setListingType(ListingType listingType) { this.listingType = listingType; }
}