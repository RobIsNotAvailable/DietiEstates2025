package com.dd25.dietiestates25.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client extends AbstractAccount
{
    //constructors

    protected Client() {}

    public Client(String email, String firstName, String lastName, String hashPassword) 
    {
        super(email, firstName, lastName, hashPassword);
    }
}
