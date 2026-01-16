package com.DD25.DietiEstates25.Model;


import com.DD25.DietiEstates25.Model.Enums.SecurityLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "company_account")
public class CompanyAccount 
{
    @Id
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "hash_password", nullable = false, length = 255)
    private String hashPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_level", nullable = false)
    private SecurityLevel security;


    //constructors

    protected CompanyAccount() {}

    public CompanyAccount(String email, String firstName, String lastName, String companyName, String hashPassword, SecurityLevel security) 
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.hashPassword = hashPassword;
        this.security = security;
    }

    //getters
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCompanyName() { return companyName; }
    public String getHashPassword() { return hashPassword; }
    public SecurityLevel getSecurity() { return security; }

    
    //setters
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setHashPassword(String hashPassword) { this.hashPassword = hashPassword; }
    public void setSecurity(SecurityLevel security) { this.security = security; }
}
