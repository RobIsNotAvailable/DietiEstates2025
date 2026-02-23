package com.dd25.dietiestates25.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "login_token")
public class LoginToken
{
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "expiration_date", nullable = false)
    private OffsetDateTime expirationDate;

    @Column(name = "is_used", nullable = false)
    private boolean used = false;

    protected LoginToken() {}

    public LoginToken(String email)
    {
        this.token = java.util.UUID.randomUUID().toString();
        this.email = email;
        this.expirationDate = OffsetDateTime.now().plusHours(24);
        this.used = false;
    }

    public boolean isValid() 
    {
        return !used && expirationDate.isAfter(OffsetDateTime.now());
    }

    //getters
    public String getToken() { return token; }
    public String getEmail() { return email; }

    //setters
    public void setUsed(boolean used) { this.used = used; }
}