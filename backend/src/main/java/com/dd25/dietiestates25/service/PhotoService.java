package com.dd25.dietiestates25.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.model.Photo;
import com.dd25.dietiestates25.model.enums.Status;
import com.dd25.dietiestates25.repository.ListingRepository;
import com.dd25.dietiestates25.repository.PhotoRepository;
import com.dd25.dietiestates25.service.utilityservice.S3Service;
import com.dd25.dietiestates25.util.SecurityUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PhotoService
{
    private final PhotoRepository repo;
    private final ListingRepository listingRepo;
    private final S3Service s3Service;
    private final SecurityUtil securityUtil;

    public void uploadPhotos(Integer listingId, List<MultipartFile> photos, List<String> descriptions)
    {
        Listing listing = listingRepo.findById(listingId).orElseThrow(() -> 
            new EntityNotFoundException("Listing not found"));
            
        if (listing.getStatus() != Status.ACTIVE)
            throw new IllegalStateException("The listing is not active");

        if (!securityUtil.getCurrentEmail().equals(listing.getAgent().getEmail()))
            throw new SecurityException("You're not the creator of the listing");

        for (int i = 0; i < photos.size(); i++)
        {
            MultipartFile photo = photos.get(i);

            String desc = (descriptions != null && i < descriptions.size()) 
                      ? descriptions.get(i) 
                      : "";
            
            String contentType = photo.getContentType();
            if (contentType == null || !contentType.startsWith("image/"))
                throw new IllegalArgumentException("Loaded file is not an image");

            String url = s3Service.uploadFile(photo);
            repo.save(new Photo(url, desc, i+1, listing));
        }
    }
}
