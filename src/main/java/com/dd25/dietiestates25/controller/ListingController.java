package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dd25.dietiestates25.dto.CreateListingRequest;
import com.dd25.dietiestates25.service.ListingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    @PostMapping("/create_listing")
    public ResponseEntity<String> createListing
    (@RequestHeader("Requester-Email") @NonNull String requesterEmail, @RequestBody @Valid CreateListingRequest request)
    {
        service.createListing(requesterEmail, request);
        return ResponseEntity.ok("Listing created successfully");
    }
}
