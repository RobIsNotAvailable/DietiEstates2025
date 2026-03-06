package com.dd25.dietiestates25.model;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.DiscriminatorFormula;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
@Inheritance(strategy = jakarta.persistence.InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_level")
@DiscriminatorFormula("CASE WHEN account_level = 'CLIENT' THEN 'client' ELSE 'company' END")
public abstract class Account implements UserDetails
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

    protected Account() {}

    protected Account(String email, String firstName, String lastName, String hashPassword) 
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

    // UserDetails methods
    @Override
    public String getUsername() { return email; }

    public String getPassword() { return hashPassword; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        String role = (this instanceof Client) ? "ROLE_CLIENT" : "ROLE_COMPANY";
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
