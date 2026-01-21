package com.dd25.dietiestates25.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dd25.dietiestates25.model.Enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "proposed_price", nullable = false)
    private BigDecimal proposedPrice;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "notes", nullable = false, length = 255)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "listing_id", nullable = false)
    private Integer listingId;

    @Column(name = "client_email", nullable = false, length = 255)
    private String clientEmail;

    @Column(name = "agent_email", nullable = false, length = 255)
    private String agentEmail;


    // Constructors

    protected Offer() {}

    public Offer(BigDecimal proposedPrice, LocalDate expirationDate, String notes, Status status, Integer listingId, String clientEmail, String agentEmail) 
    {
        this.proposedPrice = proposedPrice;
        this.expirationDate = expirationDate;
        this.notes = notes;
        this.status = status;
        this.listingId = listingId;
        this.clientEmail = clientEmail;
        this.agentEmail = agentEmail;
    }

    // Getters
    public Integer getId() { return id; }
    public BigDecimal getProposedPrice() { return proposedPrice; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public String getNotes() { return notes; }
    public Status getStatus() { return status; }
    public Integer getListingId() { return listingId; }
    public String getClientEmail() { return clientEmail; }
    public String getAgentEmail() { return agentEmail; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setProposedPrice(BigDecimal proposedPrice) { this.proposedPrice = proposedPrice; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(Status status) { this.status = status; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public void setAgentEmail(String agentEmail) { this.agentEmail = agentEmail; }
}