package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

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

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "place_id", nullable = false)
    private String placeId;         

    @Column(name = "formatted_address", nullable = false)
    private String formattedAddress; // A complete, human-readable address (given by geoapify and ready to use in UI)

    // Constructors
    public Address() {}

    public Address(String city, String street, String houseNumber, String province, String zipCode, String country,
                   Double latitude, Double longitude, String placeId, String formattedAddress) 
    {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.province = province;
        this.zipCode = zipCode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
        this.formattedAddress = formattedAddress;
    }


    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }
    public String getProvince() { return province; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getPlaceId() { return placeId; }
    public String getFormattedAddress() { return formattedAddress; }

    public void setCity(String city) { this.city = city; }
    public void setStreet(String street) { this.street = street; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public void setProvince(String province) { this.province = province; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public void setCountry(String country) { this.country = country; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }
    public void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress; }

}
