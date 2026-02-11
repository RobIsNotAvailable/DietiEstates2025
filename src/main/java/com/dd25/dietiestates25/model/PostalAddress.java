package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PostalAddress 
{
    @Column(name = "city", nullable = false)
    private String city;        

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Column(name = "province", nullable = false)
    private String province;    

    @Column(name = "zip_code", nullable = false)
    private String zipCode;    

    @Column(name = "country", nullable = false)
    private String country;     

    
    public PostalAddress() {}

    public PostalAddress(String city, String street, String houseNumber, String province, String zipCode, String country) 
    {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.province = province;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }
    public String getProvince() { return province; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }

    public void setCity(String city) { this.city = city; }
    public void setStreet(String street) { this.street = street; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public void setProvince(String province) { this.province = province; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public void setCountry(String country) { this.country = country; }
}
