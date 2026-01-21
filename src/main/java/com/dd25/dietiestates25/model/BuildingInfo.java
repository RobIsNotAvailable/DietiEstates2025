package com.dd25.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class BuildingInfo
{
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "intern", nullable = false)
    private Integer intern;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "has_elevator", nullable = false)
    private Boolean hasElevator;

    //constructor

    protected BuildingInfo() {}

    public BuildingInfo(String address, Integer intern, Integer floor, Boolean hasElevator)
    {
        this.address = address;
        this.intern = intern;
        this.floor = floor;
        this.hasElevator = hasElevator;
    }


    //getters
    public String getAddress() { return address; }
    public Integer getIntern() { return intern; }
    public Integer getFloor() { return floor; }
    public Boolean getHasElevator() { return hasElevator; }
}
