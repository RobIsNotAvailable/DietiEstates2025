package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

import com.dd25.dietiestates25.model.enums.Status;

@Entity
@Table(name = "visit_request")
public class VisitRequest 
{
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "visit_date", nullable = false)
    private OffsetDateTime visitDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;
    
    @Column(name = "listing_id", nullable = false)
    private Integer listingId;
    
    @Column(name = "client_email", nullable = false)
    private String clientEmail;
    
    @Column(name = "agent_email", nullable = false, length = 255)
    private String agentEmail;


    // Constructors

    protected VisitRequest() {}

    public VisitRequest(OffsetDateTime visitDate, Status status, Integer listingId, String clientEmail, String agentEmail) 
    {
        this.visitDate = visitDate;
        this.status = status;
        this.listingId = listingId;
        this.clientEmail = clientEmail;
        this.agentEmail = agentEmail;
    }

    // Getters
    public Integer getId() { return id; }
    public OffsetDateTime getVisitDate() { return visitDate; }
    public Status getStatus() { return status; }
    public Integer getListingId() { return listingId; }
    public String getClientEmail() { return clientEmail; }
    public String getAgentEmail() { return agentEmail; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setVisitDate(OffsetDateTime visitDate) { this.visitDate = visitDate; }
    public void setStatus(Status status) { this.status = status; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public void setAgentEmail(String agentEmail) { this.agentEmail = agentEmail; }
}