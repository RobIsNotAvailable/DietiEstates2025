package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractAccount 
{
    @Id
    @Column(name = "email", nullable = false, length = 255)
    protected String email;

    @Column(name = "first_name", nullable = false, length = 100)
    protected String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    protected String lastName;

    @Column(name = "hash_password", nullable = false, length = 255)
    protected String hashPassword;

    protected AbstractAccount() {}

    protected AbstractAccount(String email, String firstName, String lastName, String hashPassword) 
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashPassword = hashPassword;
    }

    // getters
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getHashPassword() { return hashPassword; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setHashPassword(String passwordHash) { this.hashPassword = passwordHash; }
}
