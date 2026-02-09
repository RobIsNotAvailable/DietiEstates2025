package com.dd25.dietiestates25.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyProperties 
{
    private String city;
    private String street;
    private String housenumber;
    private String state;
    private String postcode;
    private String country;
    private Double lat;
    private Double lon;
    
    @JsonProperty("place_id")
    private String placeId;
    
    private String formatted;

    private List<String> categories;

    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getHousenumber() { return housenumber; }
    public String getState() { return state; }
    public String getPostcode() { return postcode; }
    public String getCountry() { return country; }
    public Double getLat() { return lat; }
    public Double getLon() { return lon; }
    public String getPlaceId() { return placeId; }
    public String getFormatted() { return formatted; }
    public List<String> getCategories() { return categories; }

    public void setCity(String city) { this.city = city; }
    public void setStreet(String street) { this.street = street; }
    public void setHousenumber(String housenumber) { this.housenumber = housenumber; }
    public void setState(String state) { this.state = state; }
    public void setPostcode(String postcode) { this.postcode = postcode; }
    public void setCountry(String country) { this.country = country; }
    public void setLat(Double lat) { this.lat = lat; }
    public void setLon(Double lon) { this.lon = lon; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }
    public void setFormatted(String formatted) { this.formatted = formatted; }
    public void setCategories(List<String> categories) { this.categories = categories; }
}
