package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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


    @Embedded
    private Coordinates coordinates; 

    @Embedded
    private GeocodingDetails geocodingDetails;

    @Embedded
    private PostalAddress postalAddress;

    // Constructors
    public Address() {}

    public Address(PostalAddress postalAddress, Coordinates coordinates, GeocodingDetails geocodingDetails) 
    {
        this.postalAddress = postalAddress;
        this.geocodingDetails = geocodingDetails;
        this.coordinates = coordinates;
    }


    public String getCity() { return postalAddress.getCity(); }
    public String getStreet() { return postalAddress.getStreet(); }
    public String getHouseNumber() { return postalAddress.getHouseNumber(); }
    public String getProvince() { return postalAddress.getProvince(); }
    public String getZipCode() { return postalAddress.getZipCode(); }
    public String getCountry() { return postalAddress.getCountry(); }
    public Double getLatitude() { return coordinates.getLatitude(); }
    public Double getLongitude() { return coordinates.getLongitude(); }
    public String getPlaceId() { return geocodingDetails.getPlaceId(); }
    public String getFormattedAddress() { return geocodingDetails.getFormattedAddress(); }

    public void setCity(String city) { this.postalAddress.setCity(city); }
    public void setStreet(String street) { this.postalAddress.setStreet(street); }
    public void setHouseNumber(String houseNumber) { this.postalAddress.setHouseNumber(houseNumber); }
    public void setProvince(String province) { this.postalAddress.setProvince(province); }
    public void setZipCode(String zipCode) { this.postalAddress.setZipCode(zipCode); }
    public void setCountry(String country) { this.postalAddress.setCountry(country); }
    public void setLatitude(Double latitude) { this.coordinates.setLatitude(latitude); }
    public void setLongitude(Double longitude) { this.coordinates.setLongitude(longitude); }
    public void setPlaceId(String placeId) { this.geocodingDetails.setPlaceId(placeId); }
    public void setFormattedAddress(String formattedAddress) { this.geocodingDetails.setFormattedAddress(formattedAddress); }

}
