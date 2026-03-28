package com.dd25.dietiestates25.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.request.CreateListingRequest;
import com.dd25.dietiestates25.dto.request.ListingSearchRequest;
import com.dd25.dietiestates25.dto.response.FullListingResponse;
import com.dd25.dietiestates25.dto.response.ListingStatsResponse;
import com.dd25.dietiestates25.dto.response.SummaryListingResponse;
import com.dd25.dietiestates25.dto.response.SurroundingInfoResponse;
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
import com.dd25.dietiestates25.service.utilityservice.LocalizationService;
import com.dd25.dietiestates25.service.utilityservice.ListingStatsService;
import com.dd25.dietiestates25.service.utilityservice.S3Service;
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
    private final LocalizationService localizationService;
    private final SecurityUtil securityUtil;
    private final ListingStatsService statsService;
    private final S3Service s3Service;

    @Transactional
    public Integer createListing(CreateListingRequest request) 
    {
        CompanyAccount agent = agentRepo.findById(securityUtil.getCurrentEmail()).orElseThrow(() -> 
            new IllegalArgumentException(StringConstants.ACCOUNT_NOT_FOUND_MESSAGE));
        
        Address normalizedAddress = localizationService.normalizeAddress(request.rawAddress());

        SurroundingInfoResponse resp = localizationService.fetchSurroundingInfo(normalizedAddress.getLatitude(), normalizedAddress.getLongitude());
        
        SurroundingInfo surroundingInfo = new SurroundingInfo(resp.hasBus(), resp.hasPark(), resp.hasSchool());

        CommercialInfo commercialInfo = new CommercialInfo(request.price(), request.listingType());
        
        BuildingDetails buildingDetails = new BuildingDetails(normalizedAddress, request.intern(), request.floor(), request.hasElevator());
        
        HouseDetails houseDetails = new HouseDetails(request.squareMeters(), request.numberOfRooms(), request.energyClass(), request.otherServices());
        
        HouseInfo houseInfo = new HouseInfo(request.description(), buildingDetails, houseDetails);
        
        Listing listing = new Listing(request.name(), agent, commercialInfo, houseInfo, surroundingInfo);

        if (!repo.isAddressAvailable(normalizedAddress.getPlaceId(), houseInfo.getIntern())) 
            throw new IllegalStateException("This house already has an active listing");

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

        spec = spec.and(ListingSpecs.isActive());
        
        spec = spec.and(ListingSpecs.hasLocation(request.city(), request.latitude(), request.longitude()));
        
        spec = spec.and(ListingSpecs.hasPriceRange(request.minPrice(), request.maxPrice()));
        spec = spec.and(ListingSpecs.hasMinRooms(request.minRooms()));
        spec = spec.and(ListingSpecs.hasEnergyClass(request.energyClass()));
        spec = spec.and(ListingSpecs.hasListingType(request.listingType()));
        
        spec = spec.and(ListingSpecs.hasNearSchools(request.nearSchools()));
        spec = spec.and(ListingSpecs.hasNearStops(request.nearStops()));
        spec = spec.and(ListingSpecs.hasNearParks(request.nearParks()));

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
        String coverKey = l.getPhotos() != null && !l.getPhotos().isEmpty() 
            ? l.getPhotos().get(0).getFilepath() 
            : null;

        return new SummaryListingResponse
        (
            l.getId(),
            l.getName(),
            l.getCommercialInfo().getPrice(),
            l.getCommercialInfo().getListingType().toString(),
            l.getHouseInfo().getBuildingDetails().getAddress().getFormattedAddress(),
            l.getHouseInfo().getHouseDetails().getSquareMeters(),
            l.getSurroundingInfo().isNearStops(),
            l.getSurroundingInfo().isNearParks(),
            l.getSurroundingInfo().isNearSchools(),
            s3Service.generatePresignedUrl(coverKey)
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
            l.getPhotos().stream().map(p -> s3Service.generatePresignedUrl(p.getFilepath())).toList(),
            l.getPhotos().stream().map(Photo::getDescription).toList()
        );
    }

   private ListingStatsResponse mapToStats(Listing l)
    {
        ListingStatsResponse stats = repo.getStatsByListing(l.getId());
        
        if (stats != null && stats.imageUrl() != null) 
        {
            String signedUrl = s3Service.generatePresignedUrl(stats.imageUrl());
            
            return new ListingStatsResponse(
                stats.id(),
                stats.name(),
                stats.price(),
                stats.listingType(),
                stats.formattedAddress(),
                signedUrl, 
                stats.views(),
                stats.visitsRecieved(),
                stats.offersRecieved(),
                stats.highestOfferedPrice(),
                stats.lastModified(),
                stats.status(),
                stats.closurePrice()
            );
        }
        
        return stats;
    }
}