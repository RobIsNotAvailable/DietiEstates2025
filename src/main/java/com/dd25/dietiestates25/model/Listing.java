package com.dd25.dietiestates25.model;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "listing")
public class Listing 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "n_views", nullable = false)
    private Integer nViews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_email", nullable = false)
    private CompanyAccount agent;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL)
    private CommercialInfo commercialInfo;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL)
    private HouseInfo houseInfo;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL)
    private SurroundingInfo surroundingInfo;
    
    //constructors
    protected Listing() {}

    public Listing(CompanyAccount agent, CommercialInfo commercialInfo, HouseInfo houseInfo, SurroundingInfo surroundingInfo) 
    {
        this.status = Status.ACTIVE;
        this.nViews = 0;
        this.agent = agent;
        this.surroundingInfo = surroundingInfo;

        setCommercialInfo(commercialInfo);
        setHouseInfo(houseInfo);
    }

    // Getters
    public Status getStatus() { return status; }
    public Integer getNViews() { return nViews; }
    public CompanyAccount getAgent() { return agent; }
    public CommercialInfo getCommercialInfo() { return commercialInfo; }
    public HouseInfo getHouseInfo() { return houseInfo; }
    public SurroundingInfo getSurroundingInfo() { return surroundingInfo; }


    // Setters
    public void setStatus(Status status) { this.status = status; }
    public void setNViews(Integer nViews) { this.nViews = nViews; }
    public void setAgent(CompanyAccount agent) { this.agent = agent; }
    public void setSurroundingInfo(SurroundingInfo surroundingInfo) { this.surroundingInfo = surroundingInfo; }

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

}
