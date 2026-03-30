package com.dd25.dietiestates25;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dd25.dietiestates25.dto.request.CreateListingRequest;
import com.dd25.dietiestates25.dto.response.SurroundingInfoResponse;
import com.dd25.dietiestates25.model.Address;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.model.enums.ListingType;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;
import com.dd25.dietiestates25.repository.ListingRepository;
import com.dd25.dietiestates25.service.ListingService;
import com.dd25.dietiestates25.service.utilityservice.ListingStatsService;
import com.dd25.dietiestates25.service.utilityservice.LocalizationService;
import com.dd25.dietiestates25.service.utilityservice.S3Service;
import com.dd25.dietiestates25.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class ListingServiceTest
{
    
    @Mock
    private ListingRepository repo;
    
    @Mock
    private CompanyAccountRepository agentRepo;
    
    @Mock
    private LocalizationService localizationService;
    
    @Mock
    private ListingStatsService statsService;
    
    @Mock
    private SecurityUtil securityUtil;
    
    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ListingService service;

    private String name = "the house.";
    private BigDecimal price = BigDecimal.valueOf(7);
    private ListingType type = ListingType.SALE;
    private String description = "great house, walls made out of walls";
    private String rawAddress = "casa mia";
    private String intern = "20C";
    private Integer floor = 2;
    private Boolean hasElevator = true;
    private Integer sqm = 2;
    private Integer nRooms = 2;
    private String energy = "A";
    private String otherServices = "sono stanco capo";

    private Address address = new Address();
    private String placeId = "casaMiaSulSerio";
    private Double latitude = 12.34;
    private Double longitude = 12.34;

    private String email = "@example.com";
    private String firstName = "John";
    private String lastName = "";
    private String password = "notRelevant123";
    private SecurityLevel securityLevel = SecurityLevel.SUPPORT;

    @Test
    void testCreateListingSuccessTC1()
    {
        CreateListingRequest request = new CreateListingRequest(name, price, type, description, rawAddress, intern, floor, hasElevator, sqm, nRooms, energy, otherServices);
        CompanyAccount agent = new CompanyAccount(email, firstName, lastName, password, securityLevel);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        address.setPlaceId(placeId);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(agent.getEmail());
        Mockito.when(agentRepo.findById(agent.getEmail())).thenReturn(Optional.of(agent));
        Mockito.when(localizationService.normalizeAddress(request.rawAddress())).thenReturn(address);
        Mockito.when(localizationService.fetchSurroundingInfo(address.getLatitude(), address.getLongitude())).thenReturn(new SurroundingInfoResponse(true, true, true));
        Mockito.when(repo.isAddressAvailable(placeId, intern)).thenReturn(true);

        service.createListing(request);

        ArgumentCaptor<Listing> captor = ArgumentCaptor.forClass(Listing.class);
        Mockito.verify(repo).save(captor.capture());
        Listing savedListing = captor.getValue();

        assertEquals(request.name(), savedListing.getName());
        assertEquals(request.price(), savedListing.getPrice());
        assertEquals(request.listingType(), savedListing.getCommercialInfo().getListingType());
        assertEquals(request.description(), savedListing.getHouseInfo().getDescription());
        assertEquals(request.intern(), savedListing.getHouseInfo().getIntern());
        assertEquals(request.floor(), savedListing.getHouseInfo().getBuildingDetails().getFloor());
        assertEquals(request.hasElevator(), savedListing.getHouseInfo().getBuildingDetails().hasElevator());
        assertEquals(request.squareMeters(), savedListing.getHouseInfo().getHouseDetails().getSquareMeters());
        assertEquals(request.numberOfRooms(), savedListing.getHouseInfo().getHouseDetails().getSquareMeters());
        assertEquals(request.energyClass(), savedListing.getHouseInfo().getHouseDetails().getEnergyClass());
        assertEquals(request.otherServices(), savedListing.getHouseInfo().getHouseDetails().getOtherServices());
    }

    @Test
    void testCreateListingNoAgentFoundTC2()
    {
        String nonAgentEmail = "impostor@wrong.sus";
        CreateListingRequest request = new CreateListingRequest(name, price, type, description, rawAddress, intern, floor, hasElevator, sqm, nRooms, energy, otherServices);
        CompanyAccount agent = new CompanyAccount(nonAgentEmail, firstName, lastName, password, securityLevel);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        address.setPlaceId(placeId);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(agent.getEmail());
        Mockito.when(agentRepo.findById(agent.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> 
        {
            service.createListing(request);
        });

        Mockito.verify(agentRepo).findById(agent.getEmail());
    }


    @Test
    void testCreateListingNoMatchForAddressFoundTC3()
    {
        String nonValidAddress = "la luna";
        CreateListingRequest request = new CreateListingRequest(name, price, type, description, nonValidAddress, intern, floor, hasElevator, sqm, nRooms, energy, otherServices);
        CompanyAccount agent = new CompanyAccount(email, firstName, lastName, password, securityLevel);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        address.setPlaceId(placeId);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(agent.getEmail());
        Mockito.when(agentRepo.findById(agent.getEmail())).thenReturn(Optional.of(agent));
        Mockito.when(localizationService.normalizeAddress(request.rawAddress())).thenThrow(new IllegalArgumentException("Address not found"));

        assertThrows(IllegalArgumentException.class, () -> 
        {
            service.createListing(request);
        });

        Mockito.verify(agentRepo).findById(agent.getEmail());
    }

    @Test
    void testCreateListingAlreadyRegisteredTC4()
    {
        String placeIdBusy = "busyhouse";
        CreateListingRequest request = new CreateListingRequest(name, price, type, description, rawAddress, intern, floor, hasElevator, sqm, nRooms, energy, otherServices);
        CompanyAccount agent = new CompanyAccount(email, firstName, lastName, password, securityLevel);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        address.setPlaceId(placeIdBusy);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(agent.getEmail());
        Mockito.when(agentRepo.findById(agent.getEmail())).thenReturn(Optional.of(agent));
        Mockito.when(localizationService.normalizeAddress(rawAddress)).thenReturn(address);
        Mockito.when(localizationService.fetchSurroundingInfo(address.getLatitude(), address.getLongitude())).thenReturn(new SurroundingInfoResponse(true, true, true));
        Mockito.when(repo.isAddressAvailable(placeIdBusy, intern)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> 
        {
            service.createListing(request);
        });

        Mockito.verify(agentRepo).findById(agent.getEmail());
    }
}
