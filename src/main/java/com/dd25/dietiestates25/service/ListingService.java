package com.dd25.dietiestates25.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.CreateListingRequest;
import com.dd25.dietiestates25.dto.ListingSearchRequest;
import com.dd25.dietiestates25.model.Address;
import com.dd25.dietiestates25.model.BuildingDetails;
import com.dd25.dietiestates25.model.CommercialInfo;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.HouseDetails;
import com.dd25.dietiestates25.model.HouseInfo;
import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.model.SurroundingInfo;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;
import com.dd25.dietiestates25.repository.ListingRepository;
import com.dd25.dietiestates25.repository.ListingSpecs;
import com.dd25.dietiestates25.util.SecurityUtil;

import jakarta.transaction.Transactional;

@Service
public class ListingService 
{
    private final ListingRepository repo;
    private final CompanyAccountRepository agentRepo;
    private final GeoapifyService geoapifyService;
    private final SecurityUtil securityUtil;

    public ListingService(ListingRepository repo, CompanyAccountRepository agentRepo, GeoapifyService geoapifyService, SecurityUtil securityUtil)
    {
        this.repo = repo;
        this.agentRepo = agentRepo;
        this.geoapifyService = geoapifyService;
        this.securityUtil = securityUtil;
    }

    @Transactional
    public void createListing(CreateListingRequest request) 
    {
        CompanyAccount agent = agentRepo.findById(securityUtil.getCurrentEmail()).orElseThrow(() -> 
            new IllegalArgumentException("Agent account not found"));
        
        Address normalizedAddress = geoapifyService.normalizeAddress(request.rawAddress());

        SurroundingInfo surroundingInfo = geoapifyService.fetchSurroundingInfo(normalizedAddress.getLatitude(), normalizedAddress.getLongitude());

        CommercialInfo commercialInfo = new CommercialInfo(request.price(), request.listingType());
        
        BuildingDetails buildingDetails = new BuildingDetails(normalizedAddress, request.intern(), request.floor(), request.hasElevator());
        
        HouseDetails houseDetails = new HouseDetails(request.squareMeters(), request.numberOfRooms(), request.energyClass(), request.otherServices());
        
        HouseInfo houseInfo = new HouseInfo(request.description(), buildingDetails, houseDetails);
        
        Listing listing = new Listing(agent, commercialInfo, houseInfo, surroundingInfo);

        repo.save(listing);
    }

    public List<Listing> searchListings(ListingSearchRequest request)
    {
        Specification<Listing> spec = Specification.unrestricted();

        spec = spec.and(ListingSpecs.hasCity(request.city()));
        spec = spec.and(ListingSpecs.hasPriceRange(request.minPrice(), request.maxPrice()));
        spec = spec.and(ListingSpecs.hasMinRooms(request.minRooms()));
        spec = spec.and(ListingSpecs.hasEnergyClass(request.energyClass()));
        spec = spec.and(ListingSpecs.hasListingType(request.listingType()));

        return repo.findAll(spec);
    }
}