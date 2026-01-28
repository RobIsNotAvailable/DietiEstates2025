package com.dd25.dietiestates25.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client extends AbstractAccount
{
    //constructor

    public Client(String email, String firstName, String lastName, String hashPassword) 
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashPassword = hashPassword;
    }
}
