package com.dd25.dietiestates25;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.dd25.dietiestates25.model.Address;
import com.dd25.dietiestates25.model.BuildingDetails;
import com.dd25.dietiestates25.model.CommercialInfo;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.HouseDetails;
import com.dd25.dietiestates25.model.HouseInfo;
import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.model.Photo;
import com.dd25.dietiestates25.model.SurroundingInfo;
import com.dd25.dietiestates25.model.enums.ListingType;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.model.enums.Status;
import com.dd25.dietiestates25.repository.ListingRepository;
import com.dd25.dietiestates25.repository.PhotoRepository;
import com.dd25.dietiestates25.service.PhotoService;
import com.dd25.dietiestates25.service.utilityservice.S3Service;
import com.dd25.dietiestates25.util.SecurityUtil;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class PhotoServiceTest 
{
    @Mock
    private PhotoRepository repo;

    @Mock
    private ListingRepository listingRepo;

    @Mock
    private S3Service s3Service;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private PhotoService service;


    private String name = "the house.";

    private BigDecimal price = BigDecimal.valueOf(7);
    private ListingType type = ListingType.SALE;
    private CommercialInfo cInfo = new CommercialInfo(price, type);

    private String description = "great house, walls made out of walls";
    private HouseInfo hInfo = new HouseInfo(description, new BuildingDetails(new Address(), "2B", 1, true), new HouseDetails(1, 1, "Z--", "nice neighbors"));

    private SurroundingInfo sInfo = new SurroundingInfo(true, true, true);

    private List<MultipartFile> photos = new ArrayList<>();
    private List<String> descs = new ArrayList<>();
    private String photoName = "fotona.png";
    private String contentType = "image/png";
    private byte[] bytes = "yeah".getBytes();
    private String photoDesc = "the bathroom";

    private String url = "amazon/boh/lafototuabella";

    private String creatorEmail = "creator@example.com";
    private String creatorFirstName = "John";
    private String creatorLastName = "creator";
    private String creatorPassword = "notRelevant123";
    private SecurityLevel creatorSecurityLevel = SecurityLevel.AGENT;
    @Test
    void testUploadPhotoSuccessTC1()
    {
        CompanyAccount creator = new CompanyAccount(creatorEmail, creatorFirstName, creatorLastName, creatorPassword, creatorSecurityLevel);
        Listing listing = new Listing(name, creator, cInfo, hInfo, sInfo);
        MockMultipartFile file = new MockMultipartFile(photoName, photoName, contentType, bytes);
        Photo photo = new Photo(url, photoDesc, 1, listing);
        photos.add(file);
        descs.add(photoDesc);

        Mockito.when(listingRepo.findById(listing.getId())).thenReturn(Optional.of(listing));
        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(creator.getEmail());
        Mockito.when(s3Service.uploadFile(file)).thenReturn(url);

        service.uploadPhotos(listing.getId(), photos, descs);

        ArgumentCaptor<Photo> captor = ArgumentCaptor.forClass(Photo.class);

        Mockito.verify(repo).save(captor.capture());
        Mockito.verify(listingRepo).findById(listing.getId());

        Photo savedPhoto = captor.getValue();

        assertEquals(photo.getFilepath(), savedPhoto.getFilepath());
        assertEquals(photo.getDescription(), savedPhoto.getDescription());
        assertEquals(photo.getPosition(), savedPhoto.getPosition());
        assertEquals(photo.getListing(), savedPhoto.getListing());
    }

    @Test
    void testUploadPhotoNoListingFoundTC2()
    {
        CompanyAccount creator = new CompanyAccount(creatorEmail, creatorFirstName, creatorLastName, creatorPassword, creatorSecurityLevel);
        Listing listing = new Listing(name, creator, cInfo, hInfo, sInfo);
        MockMultipartFile file = new MockMultipartFile(photoName, photoName, contentType, bytes);
        photos.add(file);
        descs.add(photoDesc);

        Mockito.when(listingRepo.findById(listing.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
        {
            service.uploadPhotos(listing.getId(), photos, descs);
        });

        Mockito.verify(listingRepo).findById(listing.getId());
    }


    @Test
    void testUploadPhotoListingNotAvailableTC3()
    {
        CompanyAccount creator = new CompanyAccount(creatorEmail, creatorFirstName, creatorLastName, creatorPassword, creatorSecurityLevel);
        Listing listing = new Listing(name, creator, cInfo, hInfo, sInfo);
        listing.setStatus(Status.ENDED_SUCCESFULLY);
        MockMultipartFile file = new MockMultipartFile(photoName, photoName, contentType, bytes);
        photos.add(file);
        descs.add(photoDesc);

        Mockito.when(listingRepo.findById(listing.getId())).thenReturn(Optional.of(listing));

        assertThrows(IllegalStateException.class, () -> 
        {
            service.uploadPhotos(listing.getId(), photos, descs);
        });

        Mockito.verify(listingRepo).findById(listing.getId());
    }


    @Test
    void testUploadPhotoNotTheOwnerTC4()
    {
        String tampererEmail = "criminal@hehe.com";
        CompanyAccount creator = new CompanyAccount(creatorEmail, creatorFirstName, creatorLastName, creatorPassword, creatorSecurityLevel);
        Listing listing = new Listing(name, creator, cInfo, hInfo, sInfo);
        MockMultipartFile file = new MockMultipartFile(photoName, photoName, contentType, bytes);
        photos.add(file);
        descs.add(photoDesc);

        Mockito.when(listingRepo.findById(listing.getId())).thenReturn(Optional.of(listing));
        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(tampererEmail);

        assertThrows(SecurityException.class, () -> 
        {
            service.uploadPhotos(listing.getId(), photos, descs);
        });

        Mockito.verify(listingRepo).findById(listing.getId());
    }


    @Test
    void testUploadPhotoNotAPhotoTC5()
    {
        String maliciousContent = "README.virus";
        CompanyAccount creator = new CompanyAccount(creatorEmail, creatorFirstName, creatorLastName, creatorPassword, creatorSecurityLevel);
        Listing listing = new Listing(name, creator, cInfo, hInfo, sInfo);
        MockMultipartFile file = new MockMultipartFile(photoName, photoName, maliciousContent, bytes);
        photos.add(file);
        descs.add(photoDesc);

        Mockito.when(listingRepo.findById(listing.getId())).thenReturn(Optional.of(listing));
        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(creator.getEmail());

        assertThrows(IllegalArgumentException.class, () -> 
        {
            service.uploadPhotos(listing.getId(), photos, descs);
        });

        Mockito.verify(listingRepo).findById(listing.getId());
    }
}
