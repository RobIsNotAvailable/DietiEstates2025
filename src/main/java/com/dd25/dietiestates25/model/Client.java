package com.dd25.dietiestates25.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("client")
public class Client extends Account
{
    //constructors
    protected Client() {}

    public Client(String email, String firstName, String lastName, String hashPassword) 
    {
        super(email, firstName, lastName, hashPassword);
    }
}
