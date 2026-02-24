package com.dd25.dietiestates25.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dd25.dietiestates25.dto.CreateListingRequest;
import com.dd25.dietiestates25.dto.FullListingResponse;
import com.dd25.dietiestates25.dto.ListingSearchRequest;
import com.dd25.dietiestates25.dto.SummaryListingResponse;
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
    public ResponseEntity<String> create(@RequestBody @Valid CreateListingRequest request)
    {
        service.createListing(request);
        return ResponseEntity.ok("Listing created successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<SummaryListingResponse>> search(ListingSearchRequest request)
    {
        List<SummaryListingResponse> response = service.searchListings(request); 
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullListingResponse> view(@PathVariable Integer id)
    {
        FullListingResponse response = service.getListingById(id);
        return ResponseEntity.ok(response);
    }
}
