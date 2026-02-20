package com.dd25.dietiestates25.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.dd25.dietiestates25.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
@Entity
@Table(name = "offer")
public class Offer extends AbstractProposal
{
    @Column(name = "proposed_price", nullable = false)
    private BigDecimal proposedPrice;

    @Column(name = "expiration_date", nullable = false)
    private OffsetDateTime expirationDate;

    @Column(name = "notes", nullable = false, length = 255)
    private String notes;

    // Constructors

    protected Offer() {}

    public Offer(BigDecimal proposedPrice, OffsetDateTime expirationDate, String notes, Status status, Listing listing, Client client, CompanyAccount agent) 
    {
        super(status, listing, client, agent);
        this.proposedPrice = proposedPrice;
        this.expirationDate = expirationDate;
        this.notes = notes;
    }

    // Getters
    public BigDecimal getProposedPrice() { return proposedPrice; }
    public OffsetDateTime getExpirationDate() { return expirationDate; }
    public String getNotes() { return notes; }

    // Setters
    public void setProposedPrice(BigDecimal proposedPrice) { this.proposedPrice = proposedPrice; }
    public void setExpirationDate(OffsetDateTime expirationDate) { this.expirationDate = expirationDate; }
    public void setNotes(String notes) { this.notes = notes; }
}