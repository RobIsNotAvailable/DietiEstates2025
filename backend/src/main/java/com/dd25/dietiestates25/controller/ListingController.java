package com.dd25.dietiestates25.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dd25.dietiestates25.dto.request.CreateListingRequest;
import com.dd25.dietiestates25.dto.request.ListingSearchRequest;
import com.dd25.dietiestates25.dto.response.FullListingResponse;
import com.dd25.dietiestates25.dto.response.ListingStatsResponse;
import com.dd25.dietiestates25.dto.response.StringResponse;
import com.dd25.dietiestates25.dto.response.SummaryListingResponse;
import com.dd25.dietiestates25.service.ListingService;
import com.dd25.dietiestates25.service.PhotoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "*") 
@RequiredArgsConstructor
public class ListingController
{
    private final ListingService service;
    private final PhotoService photoService;

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody @Valid CreateListingRequest request)
    {
        Integer id = service.createListing(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/photos/{id}")
    public ResponseEntity<StringResponse> uploadPhotos(@PathVariable Integer id, @RequestParam("photos") List<MultipartFile> photos, @RequestParam("descriptions") List<String> descriptions)
    {
        photoService.uploadPhotos(id, photos, descriptions);
        return ResponseEntity.status(HttpStatus.CREATED).body(new StringResponse("Photos uploaded successfully"));
    }
    

    @GetMapping("/view/{id}")
    public ResponseEntity<FullListingResponse> view(@PathVariable Integer id)
    {
        FullListingResponse response = service.getListingById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SummaryListingResponse>> search(ListingSearchRequest request)
    {
        List<SummaryListingResponse> response = service.searchListings(request); 
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ListingStatsResponse>> stats()
    {
        List<ListingStatsResponse> response = service.getStats(); 
        return ResponseEntity.ok(response);
    }
}
