package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

import com.dd25.dietiestates25.model.enums.Status;

@Entity
@Table(name = "visit_request")
public class VisitRequest extends AbstractProposal
{
    @Column(name = "visit_date", nullable = false)
    private OffsetDateTime visitDate;
    
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
    public OffsetDateTime getVisitDate() { return visitDate; }

    // Setters
    public void setVisitDate(OffsetDateTime visitDate) { this.visitDate = visitDate; }
}