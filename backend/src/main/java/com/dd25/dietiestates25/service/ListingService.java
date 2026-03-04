package com.dd25.dietiestates25.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.CreateListingRequest;
import com.dd25.dietiestates25.dto.FullListingResponse;
import com.dd25.dietiestates25.dto.ListingSearchRequest;
import com.dd25.dietiestates25.dto.ListingStatsResponse;
import com.dd25.dietiestates25.dto.SummaryListingResponse;
import com.dd25.dietiestates25.model.Address;
import com.dd25.dietiestates25.model.BuildingDetails;
import com.dd25.dietiestates25.model.CommercialInfo;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.HouseDetails;
import com.dd25.dietiestates25.model.HouseInfo;
import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.model.Photo;
import com.dd25.dietiestates25.model.SurroundingInfo;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;
import com.dd25.dietiestates25.repository.ListingRepository;
import com.dd25.dietiestates25.repository.ListingSpecs;
import com.dd25.dietiestates25.service.utilityservice.GeoapifyService;
import com.dd25.dietiestates25.service.utilityservice.ListingStatsService;
import com.dd25.dietiestates25.util.SecurityUtil;
import com.dd25.dietiestates25.util.StringConstants;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ListingService 
{
    private final ListingRepository repo;
    private final CompanyAccountRepository agentRepo;
    private final GeoapifyService geoapifyService;
    private final SecurityUtil securityUtil;
    private final ListingStatsService statsService;

    @Transactional
    public Integer createListing(CreateListingRequest request) 
    {
        CompanyAccount agent = agentRepo.findById(securityUtil.getCurrentEmail()).orElseThrow(() -> 
            new IllegalArgumentException(StringConstants.ACCOUNT_NOT_FOUND_MESSAGE));
        
        Address normalizedAddress = geoapifyService.normalizeAddress(request.rawAddress());

        SurroundingInfo surroundingInfo = geoapifyService.fetchSurroundingInfo(normalizedAddress.getLatitude(), normalizedAddress.getLongitude());

        CommercialInfo commercialInfo = new CommercialInfo(request.price(), request.listingType());
        
        BuildingDetails buildingDetails = new BuildingDetails(normalizedAddress, request.intern(), request.floor(), request.hasElevator());
        
        HouseDetails houseDetails = new HouseDetails(request.squareMeters(), request.numberOfRooms(), request.energyClass(), request.otherServices());
        
        HouseInfo houseInfo = new HouseInfo(request.description(), buildingDetails, houseDetails);
        
        Listing listing = new Listing(request.name(), agent, commercialInfo, houseInfo, surroundingInfo);

        repo.save(listing);

        return listing.getId();
    }

    public FullListingResponse getListingById(Integer id) 
    {
        Listing listing = repo.findById(id).orElseThrow(() -> 
            new EntityNotFoundException("Listing not found"));
        
        statsService.incrementViews(id);
        return mapToFull(listing);
    }

    public List<SummaryListingResponse> searchListings(ListingSearchRequest request)
    {
        Specification<Listing> spec = Specification.unrestricted();

        spec = spec.and(ListingSpecs.isActive(true));
        spec = spec.and(ListingSpecs.hasCity(request.city()));
        spec = spec.and(ListingSpecs.hasPriceRange(request.minPrice(), request.maxPrice()));
        spec = spec.and(ListingSpecs.hasMinRooms(request.minRooms()));
        spec = spec.and(ListingSpecs.hasEnergyClass(request.energyClass()));
        spec = spec.and(ListingSpecs.hasListingType(request.listingType()));
        List<Listing> results = repo.findAll(spec);
        return results.stream()
                  .map(this::mapToSummary)
                  .toList();
    }

    public List<ListingStatsResponse>getStats()
    {
        List<Listing> results = repo.findByAgent(securityUtil.getCurrentEmail());
        return results.stream()
            .map(this::mapToStats)
            .toList();
    }

    private SummaryListingResponse mapToSummary(Listing l) 
    {
        return new SummaryListingResponse
        (
            l.getId(),
            l.getName(),
            l.getCommercialInfo().getPrice(),
            l.getCommercialInfo().getListingType().toString(),
            l.getHouseInfo().getBuildingDetails().getAddress().getFormattedAddress(),
            l.getHouseInfo().getHouseDetails().getSquareMeters(),
            l.getHouseInfo().getDescription(),
            l.getSurroundingInfo().isNearStops(),
            l.getSurroundingInfo().isNearParks(),
            l.getSurroundingInfo().isNearSchools(),
            l.getPhotos().stream().map(Photo::getFilepath).findFirst().orElse(null)
        );
    }

    private FullListingResponse mapToFull(Listing l) 
    {
        return new FullListingResponse
        (
            l.getName(),
            l.getAgent().getEmail(),
            l.getAgent().getFirstName() + " " + l.getAgent().getLastName(),
            l.getCommercialInfo().getPrice(),
            l.getCommercialInfo().getListingType().toString(),
            l.getHouseInfo().getBuildingDetails().getAddress().getFormattedAddress(),
            l.getHouseInfo().getBuildingDetails().getIntern(),
            l.getHouseInfo().getBuildingDetails().getFloor(),
            l.getHouseInfo().getBuildingDetails().hasElevator(),
            l.getHouseInfo().getHouseDetails().getSquareMeters(),
            l.getHouseInfo().getHouseDetails().getNumberOfRooms(),
            l.getHouseInfo().getHouseDetails().getEnergyClass(),
            l.getHouseInfo().getHouseDetails().getOtherServices(),
            l.getHouseInfo().getDescription(),
            l.getSurroundingInfo().isNearStops(),
            l.getSurroundingInfo().isNearParks(),
            l.getSurroundingInfo().isNearSchools(),
            l.getPhotos().stream().map(Photo::getFilepath).toList()
        );
    }

    private ListingStatsResponse mapToStats(Listing l)
    {
        return repo.getStatsByListing(l.getId());
    }
}