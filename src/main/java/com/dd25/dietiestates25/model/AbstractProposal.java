package com.dd25.dietiestates25.model;

import com.dd25.dietiestates25.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractProposal 
{
    @Id
    @Column(name = "id")
    protected Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    protected Status status = Status.ACTIVE;
    
    @Column(name = "listing_id", nullable = false)
    protected Integer listingId;
    
    @Column(name = "client_email", nullable = false)
    protected String clientEmail;
    
    @Column(name = "agent_email", nullable = false, length = 255)
    protected String agentEmail;

    protected AbstractProposal() {}

    // getters
    public Integer getId() { return id; }
    public Status getStatus() { return status; }
    public Integer getListingId() { return listingId; }
    public String getClientEmail() { return clientEmail; }
    public String getAgentEmail() { return agentEmail; }

    // setters
    public void setId(Integer id) { this.id = id; }
    public void setStatus(Status status) { this.status = status; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public void setAgentEmail(String agentEmail) { this.agentEmail = agentEmail; }
}