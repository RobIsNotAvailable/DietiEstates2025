package com.dd25.dietiestates25.model;


import com.dd25.dietiestates25.model.enums.SecurityLevel;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("COMPANY")
public class CompanyAccount extends Account
{
    @Enumerated(EnumType.STRING)
    @Column(name = "security_level", nullable = false)
    private SecurityLevel securityLevel;

    //constructors

    protected CompanyAccount() {}

    public CompanyAccount(String email, String firstName, String lastName, String hashPassword, SecurityLevel securityLevel) 
    {
        super(email, firstName, lastName, hashPassword);
        this.securityLevel = securityLevel;
    }

    //getters
    public SecurityLevel getSecurityLevel() { return securityLevel; }

    
    //setters
    public void setSecurityLevel(SecurityLevel securityLevel) { this.securityLevel = securityLevel; }
}
