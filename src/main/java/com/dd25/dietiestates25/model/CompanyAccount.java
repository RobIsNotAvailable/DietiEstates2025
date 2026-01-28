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
    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_level", nullable = false)
    private SecurityLevel securityLevel;

    @Column(name = "changed_password", nullable = false)
    private boolean changedPassword = true;

    //constructors

    protected CompanyAccount() {}

    public CompanyAccount(String email, String firstName, String lastName, String companyName, String hashPassword, SecurityLevel securityLevel) 
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.hashPassword = hashPassword;
        this.securityLevel = securityLevel;
    }

    //getters
    public String getCompanyName() { return companyName; }
    public SecurityLevel getSecurityLevel() { return securityLevel; }
    public boolean isChangedPassword() { return changedPassword; }

    
    //setters
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setSecurityLevel(SecurityLevel securityLevel) { this.securityLevel = securityLevel; }
    public void setChangedPassword(boolean changedPassword) { this.changedPassword = changedPassword; }
}
