package com.dd25.dietiestates25.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.CreateListingRequest;
import com.dd25.dietiestates25.model.BuildingDetails;
import com.dd25.dietiestates25.model.CommercialInfo;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.HouseDetails;
import com.dd25.dietiestates25.model.HouseInfo;
import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;
import com.dd25.dietiestates25.repository.ListingRepository;

@Service
public class ListingService 
{
    private final ListingRepository repo;
    private final CompanyAccountRepository agentRepo;

    public ListingService(ListingRepository repo, CompanyAccountRepository agentRepo)
    {
        this.repo = repo;
        this.agentRepo = agentRepo;
    }

    public void createListing(@NonNull String requesterEmail, CreateListingRequest request)
    {
        CompanyAccount agent = agentRepo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Agent account not found"));
        
        CommercialInfo commercialInfo = new CommercialInfo(request.price(), request.listingType());
        
        BuildingDetails buildingDetails = new BuildingDetails(request.address(), request.intern(), request.floor(), request.hasElevator());
        HouseDetails houseDetails = new HouseDetails(request.squareMeters(), request.numberOfRooms(), request.energyClass(), request.otherServices());
        HouseInfo houseInfo = new HouseInfo(request.description(), buildingDetails, houseDetails);
        
        Listing listing = new Listing(agent, commercialInfo, houseInfo);

        repo.save(listing);
    }
}