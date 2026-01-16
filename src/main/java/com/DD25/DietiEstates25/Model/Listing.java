package com.DD25.DietiEstates25.Model;

import com.DD25.DietiEstates25.Model.Enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "listing")
public class Listing 
{
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "n_views", nullable = false)
    private Integer nViews;

    @Column(name = "agent_email", nullable = false, length = 255)
    private String agentEmail;

    //constructors

    protected Listing() {}

    public Listing(Long id, Status status, Integer nViews, String agentEmail) 
    {
        this.id = id;
        this.status = status;
        this.nViews = nViews;
        this.agentEmail = agentEmail;
    }

    // Getters
    public Long getId() { return id; }
    public Status getStatus() { return status; }
    public Integer getNViews() { return nViews; }
    public String getAgentEmail() { return agentEmail; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setStatus(Status status) { this.status = status; }
    public void setNViews(Integer nViews) { this.nViews = nViews; }
    public void setAgentEmail(String agentEmail) { this.agentEmail = agentEmail; }
}
