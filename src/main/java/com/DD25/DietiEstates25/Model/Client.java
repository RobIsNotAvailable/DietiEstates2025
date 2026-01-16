package com.DD25.DietiEstates25.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client
{
    @Id
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "hash_password", nullable = false, length = 255)
    private String passwordHash;


    //constructors

    protected Client() {}

    public Client(String email, String firstName, String lastName, String passwordHash) 
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
    }


    // getters
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPasswordHash() { return passwordHash; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
