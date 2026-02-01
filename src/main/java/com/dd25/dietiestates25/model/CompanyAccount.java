package com.dd25.dietiestates25.model;


import com.dd25.dietiestates25.model.enums.SecurityLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "company_account")
public class CompanyAccount extends AbstractAccount
{
    @Enumerated(EnumType.STRING)
    @Column(name = "security_level", nullable = false)
    private SecurityLevel securityLevel;

    @Column(name = "password_changed", nullable = false)
    private boolean passwordChanged = true;

    //constructors

    protected CompanyAccount() {}

    public CompanyAccount(String email, String firstName, String lastName, String hashPassword, SecurityLevel securityLevel) 
    {
        super(email, firstName, lastName, hashPassword);
        this.securityLevel = securityLevel;
    }

    //getters
    public SecurityLevel getSecurityLevel() { return securityLevel; }
    public boolean isPasswordChanged() { return passwordChanged; }

    
    //setters
    public void setSecurityLevel(SecurityLevel securityLevel) { this.securityLevel = securityLevel; }
    public void setPasswordChanged(boolean passwordChanged) { this.passwordChanged = passwordChanged; }
}
