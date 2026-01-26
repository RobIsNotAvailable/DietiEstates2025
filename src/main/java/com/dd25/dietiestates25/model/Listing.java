package com.dd25.dietiestates25.model;

import com.dd25.dietiestates25.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "listing")
public class Listing 
{
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "n_views", nullable = false)
    private Integer nViews;

    @Column(name = "agent_email", nullable = false, length = 255)
    private String agentEmail;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "listing_id")
    private CommercialInfo commercialInfo;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "listing_id")
    private HouseInfo houseInfo;
    
    //constructors

    protected Listing() {}

    public Listing(Integer id, Status status, Integer nViews, String agentEmail, CommercialInfo commercialInfo, HouseInfo houseInfo) 
    {
        this.id = id;
        this.status = status;
        this.nViews = nViews;
        this.agentEmail = agentEmail;
        this.commercialInfo = commercialInfo;
        this.houseInfo = houseInfo;
    }

    // Getters
    public Integer getId() { return id; }
    public Status getStatus() { return status; }
    public Integer getNViews() { return nViews; }
    public String getAgentEmail() { return agentEmail; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setStatus(Status status) { this.status = status; }
    public void setNViews(Integer nViews) { this.nViews = nViews; }
    public void setAgentEmail(String agentEmail) { this.agentEmail = agentEmail; }
}
