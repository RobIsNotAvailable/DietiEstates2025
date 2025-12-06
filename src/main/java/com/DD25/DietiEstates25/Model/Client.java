package com.DD25.DietiEstates25.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client 
{
    @Id
    private String email;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "hash_password")
    private String hashPassword;

    public Client(String email, String firstName, String lastName, String hashPassword)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashPassword = hashPassword;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getHashPassword()
    {
        return hashPassword;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String hashPassword)
    {
        this.hashPassword = hashPassword;
    }
}