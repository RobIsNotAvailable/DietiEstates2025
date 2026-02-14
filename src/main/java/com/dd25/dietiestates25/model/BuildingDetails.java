package com.dd25.dietiestates25.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;



@Embeddable
public class BuildingDetails
{
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "intern", nullable = false)
    private Integer intern;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "elevator", nullable = false)
    private Boolean elevator;

    //constructor

    protected BuildingDetails() {}

    public BuildingDetails(Address address, Integer intern, Integer floor, Boolean elevator)
    {
        this.address = address;
        this.intern = intern;
        this.floor = floor;
        this.elevator = elevator;
    }


    //getters
    public Address getAddress() { return address; }
    public Integer getIntern() { return intern; }
    public Integer getFloor() { return floor; }
    public Boolean isElevator() { return elevator; }

    //setters
    public void setAddress(Address address) { this.address = address; }
    public void setIntern(Integer intern) { this.intern = intern; }
    public void setFloor(Integer floor) { this.floor = floor; }
    public void setElevator(Boolean elevator) { this.elevator = elevator; }
}
