package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class BuildingDetails
{
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "intern", nullable = false)
    private Integer intern;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "elevator", nullable = false)
    private Boolean elevator;

    //constructor

    protected BuildingDetails() {}

    public BuildingDetails(String address, Integer intern, Integer floor, Boolean elevator)
    {
        this.address = address;
        this.intern = intern;
        this.floor = floor;
        this.elevator = elevator;
    }


    //getters
    public String getAddress() { return address; }
    public Integer getIntern() { return intern; }
    public Integer getFloor() { return floor; }
    public Boolean isElevator() { return elevator; }

    //setters
    public void setAddress(String address) { this.address = address; }
    public void setIntern(Integer intern) { this.intern = intern; }
    public void setFloor(Integer floor) { this.floor = floor; }
    public void setElevator(Boolean elevator) { this.elevator = elevator; }
}
