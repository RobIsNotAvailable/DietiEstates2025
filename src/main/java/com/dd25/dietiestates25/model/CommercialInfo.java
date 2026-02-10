package com.dd25.dietiestates25.model;

import java.math.BigDecimal;

import com.dd25.dietiestates25.model.enums.ListingType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "commercial_info")
public class CommercialInfo 
{
    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "listing_type", nullable = false)
    private ListingType listingType;

    // Constructors

    protected CommercialInfo() {}

    public CommercialInfo(BigDecimal price, ListingType listingType) 
    {
        this.price = price;
        this.listingType = listingType;
    }

    // Getters
    public Listing getListing() { return listing; }
    public BigDecimal getPrice() { return price; }
    public ListingType getListingType() { return listingType; }

    // Setters
    public void setListing(Listing listing) { this.listing = listing; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setListingType(ListingType listingType) { this.listingType = listingType; }
}