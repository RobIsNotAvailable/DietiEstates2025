package com.dd25.dietiestates25.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dd25.dietiestates25.model.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "listing")
public class Listing 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "views", nullable = false)
    private Integer views;

    @Column(name = "last_modified", nullable = false)
    private OffsetDateTime lastModified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_email", nullable = false)
    private CompanyAccount agent;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL)
    private CommercialInfo commercialInfo;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL)
    private HouseInfo houseInfo;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL)
    private SurroundingInfo surroundingInfo;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private List<Photo> photos = new ArrayList<>();
    
    //constructors
    protected Listing() {}

    public Listing(String name, CompanyAccount agent, CommercialInfo commercialInfo, HouseInfo houseInfo, SurroundingInfo surroundingInfo) 
    {
        this.name = name;
        this.status = Status.ACTIVE;
        this.views = 0;
        this.lastModified = OffsetDateTime.now();
        this.agent = agent;

        setCommercialInfo(commercialInfo);
        setHouseInfo(houseInfo);
        setSurroundingInfo(surroundingInfo);
    }

    // Getters
    public Integer getId() { return id; }
    public Status getStatus() { return status; }
    public Integer getViews() { return views; }
    public String getName() { return name; }
    public OffsetDateTime getLastModified() { return lastModified; }
    public CompanyAccount getAgent() { return agent; }
    public CommercialInfo getCommercialInfo() { return commercialInfo; }
    public HouseInfo getHouseInfo() { return houseInfo; }   
    public SurroundingInfo getSurroundingInfo() { return surroundingInfo; }
    public List<Photo> getPhotos() { return photos; }


    // Setters
    public void setStatus(Status status) { this.status = status; }
    public void setViews(Integer views) { this.views = views; }

    public void setCommercialInfo(CommercialInfo commercialInfo)
    {
        commercialInfo.setListing(this);
        this.commercialInfo = commercialInfo;
    }

    public void setHouseInfo(HouseInfo houseInfo)
    {
        houseInfo.setListing(this);
        this.houseInfo = houseInfo;
    }

    public void setSurroundingInfo(SurroundingInfo surroundingInfo) 
    {
        surroundingInfo.setListing(this);
        this.surroundingInfo = surroundingInfo;
    }

}
