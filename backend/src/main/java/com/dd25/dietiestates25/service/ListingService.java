package com.dd25.dietiestates25.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
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
import com.dd25.dietiestates25.service.utilityservice.GeoapifyService;
import com.dd25.dietiestates25.service.utilityservice.ListingStatsService;
import com.dd25.dietiestates25.util.SecurityUtil;
import com.dd25.dietiestates25.util.StringConstants;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

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
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket.name}") 
    private String bucketName;

    @Transactional
    public Integer createListing(CreateListingRequest request) 
    {
        CompanyAccount agent = agentRepo.findById(securityUtil.getCurrentEmail()).orElseThrow(() -> 
            new IllegalArgumentException(StringConstants.ACCOUNT_NOT_FOUND_MESSAGE));
        
        Address normalizedAddress = geoapifyService.normalizeAddress(request.rawAddress());

        SurroundingInfoResponse resp = geoapifyService.fetchSurroundingInfo(normalizedAddress.getLatitude(), normalizedAddress.getLongitude());
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
    
    public Page<SummaryListingResponse> getAllActiveListings(int page, int size) 
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModified").descending());

        Page<Listing> entityPage = repo.findAllActiveWithDetails(pageable);

        return entityPage.map(this::mapToSummary);
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

        String signedUrl = generatePresignedUrl(coverKey);

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
            signedUrl 
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
            l.getPhotos().stream().map(p -> generatePresignedUrl(p.getFilepath())).toList(),
            l.getPhotos().stream().map(Photo::getDescription).toList()
        );
    }

    private ListingStatsResponse mapToStats(Listing l)
    {
        return repo.getStatsByListing(l.getId());
    }

    private String generatePresignedUrl(String fullUrlOrKey) 
    {
        if (fullUrlOrKey == null || fullUrlOrKey.isEmpty()) 
        {
            return null;
        }

        String objectKey = fullUrlOrKey.contains("/") 
                ? fullUrlOrKey.substring(fullUrlOrKey.lastIndexOf("/") + 1) 
                : fullUrlOrKey;

        try 
        {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName) 
                    .key(objectKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(java.time.Duration.ofMinutes(60)) 
                    .getObjectRequest(getObjectRequest)
                    .build();

            return s3Presigner.presignGetObject(presignRequest).url().toString();
        } 
        catch (Exception e) 
        {
            return null;
        }
    }
}