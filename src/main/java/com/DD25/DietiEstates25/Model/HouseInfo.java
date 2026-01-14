package com.DD25.DietiEstates25.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "house_info")
public class HouseInfo 
{
    @Id
    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "intern", nullable = false)
    private Integer intern;

    @Column(name = "square_meters", nullable = false)
    private Integer squareMeters;

    @Column(name = "n_rooms", nullable = false)
    private Integer numberOfRooms;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "has_elevator", nullable = false)
    private Boolean hasElevator;

    @Column(name = "other_services", nullable = false)
    private String otherServices;

    @Column(name = "energy_class", nullable = false, length = 5)
    private String energyClass;

    // Constructors
    public HouseInfo() {}

    public HouseInfo(Integer listingId, String description, String address, Integer intern, Integer squareMeters,
                     Integer numberOfRooms, Integer floor, Boolean hasElevator, String otherServices, String energyClass) 
    {
        this.listingId = listingId;
        this.description = description;
        this.address = address;
        this.intern = intern;
        this.squareMeters = squareMeters;
        this.numberOfRooms = numberOfRooms;
        this.floor = floor;
        this.hasElevator = hasElevator;
        this.otherServices = otherServices;
        this.energyClass = energyClass;
    }

    // Getters
    public Integer getListingId() { return listingId; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public Integer getIntern() { return intern; }
    public Integer getSquareMeters() { return squareMeters; }
    public Integer getNumberOfRooms() { return numberOfRooms; }
    public Integer getFloor() { return floor; }
    public Boolean getHasElevator() { return hasElevator; }
    public String getOtherServices() { return otherServices; }
    public String getEnergyClass() { return energyClass; }

    // Setters
    public void setListingId(Integer listingId) { this.listingId = listingId;}
    public void setDescription(String description) { this.description = description; }
    public void setAddress(String address) { this.address = address; }
    public void setIntern(Integer intern) { this.intern = intern; }
    public void setSquareMeters(Integer squareMeters) { this.squareMeters = squareMeters; }
    public void setNumberOfRooms(Integer numberOfRooms) { this.numberOfRooms = numberOfRooms; }
    public void setFloor(Integer floor) { this.floor = floor; }
    public void setHasElevator(Boolean hasElevator) { this.hasElevator = hasElevator; }
    public void setOtherServices(String otherServices) { this.otherServices = otherServices; }
    public void setEnergyClass(String energyClass) { this.energyClass = energyClass; }
}
