package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class HouseDetails
{
    @Column(name = "square_meters", nullable = false)
    private Integer squareMeters;

    @Column(name = "n_rooms", nullable = false)
    private Integer numberOfRooms;

    @Column(name = "energy_class", nullable = false, length = 5)
    private String energyClass;

    @Column(name = "other_services", nullable = false)
    private String otherServices;

    protected HouseDetails() {}

    public HouseDetails(Integer squareMeters, Integer numberOfRooms, String energyClass, String otherServices)
    {
        this.squareMeters = squareMeters;
        this.numberOfRooms = numberOfRooms;
        this.energyClass = energyClass;
        this.otherServices = otherServices;
    }

    //getters
    public Integer getSquareMeters() { return squareMeters; }
    public Integer getNumberOfRooms() { return numberOfRooms; }
    public String getEnergyClass() { return energyClass; }
    public String getOtherServices() { return otherServices; }

    //setters
    public void setSquareMeters(Integer squareMeters) { this.squareMeters = squareMeters; }
    public void setNumberOfRooms(Integer numberOfRooms) { this.numberOfRooms = numberOfRooms; }
    public void setEnergyClass(String energyClass) { this.energyClass = energyClass; }
    public void setOtherServices(String otherServices) { this.otherServices = otherServices; }
}