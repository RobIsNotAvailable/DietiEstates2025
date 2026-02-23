package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "photo")
public class Photo 
{
    @Id
    @Column(name = "filepath", nullable = false, length = 255)
    private String filepath;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "position", nullable = false)
    private int position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id")
    private Listing listing;


    // Constructors 
    
    protected Photo() {}
    
    public Photo(String filepath, String description, int position, Listing listing) 
    {
        this.filepath = filepath;
        this.description = description;
        this.position = position;
        this.listing = listing;
    }

    // Getters 
    public String getFilepath() { return filepath; }
    public String getDescription() { return description; }
    public int getPosition() { return position; }
    public Listing getListing() { return listing; }

    // Setters
    public void setFilepath(String filepath) { this.filepath = filepath; }
    public void setDescription(String description) { this.description = description; }
    public void setPosition(int position) { this.position = position; }
    public void setListing(Listing listing) { this.listing = listing; }
}
