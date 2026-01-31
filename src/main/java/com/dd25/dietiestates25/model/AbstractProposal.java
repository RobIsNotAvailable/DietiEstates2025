package com.dd25.dietiestates25.model;

import com.dd25.dietiestates25.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractProposal 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    protected Status status = Status.ACTIVE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    protected Listing listing;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_email", nullable = false)
    protected Client client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_email", nullable = false)
    protected CompanyAccount agent;

    protected AbstractProposal() {}

    protected AbstractProposal(Status status, Listing listing, Client client, CompanyAccount agent) 
    {
        this.status = status;
        this.listing = listing;
        this.client = client;
        this.agent = agent;
    }

    // getters
    public Integer getId() { return id; }
    public Status getStatus() { return status; }
    public Listing getListingId() { return listing; }
    public Client getClient() { return client; }
    public CompanyAccount getAgent() { return agent; }

    // setters
    public void setId(Integer id) { this.id = id; }
    public void setStatus(Status status) { this.status = status; }
    public void setListingId(Listing listing) { this.listing = listing; }
    public void setClient(Client client) { this.client = client; }
    public void setAgent(CompanyAccount agent) { this.agent = agent; }
}