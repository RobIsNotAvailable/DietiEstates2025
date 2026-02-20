package com.dd25.dietiestates25.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dd25.dietiestates25.dto.CreateListingRequest;
import com.dd25.dietiestates25.dto.ListingSearchRequest;
import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.service.ListingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/listings")
public class ListingController
{
    private final ListingService service;

    public ListingController(ListingService service)
    {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createListing(@RequestBody @Valid CreateListingRequest request)
    {
        service.createListing(request);
        return ResponseEntity.ok("Listing created successfully");
    }

    @PostMapping("/search")
    public ResponseEntity<List<Listing>> search(@RequestBody ListingSearchRequest request)
    {
        List<Listing> results = service.searchListings(request);
        
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(results);
    }
}
