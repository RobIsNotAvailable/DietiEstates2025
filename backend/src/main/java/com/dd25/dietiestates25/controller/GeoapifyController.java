package com.dd25.dietiestates25.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dd25.dietiestates25.dto.GeoapifyProperties;
import com.dd25.dietiestates25.dto.response.SurroundingInfoResponse;
import com.dd25.dietiestates25.model.SurroundingInfo;
import com.dd25.dietiestates25.service.utilityservice.GeoapifyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/location") 
@RequiredArgsConstructor
public class GeoapifyController 
{
    private final GeoapifyService geoapifyService;

    @GetMapping("/suggestions")
    public ResponseEntity<List<GeoapifyProperties>> getSuggestions(@RequestParam String rawAddress) 
    {
        List<GeoapifyProperties> suggestions = geoapifyService.getPossibleAddresses(rawAddress);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/surroundings")
    public ResponseEntity<SurroundingInfoResponse> getSurroundings(@RequestParam double lat, @RequestParam double lon) 
    {
        SurroundingInfo info = geoapifyService.fetchSurroundingInfo(lat, lon);
        
        SurroundingInfoResponse dto = new SurroundingInfoResponse
        (
            info.isNearStops(),
            info.isNearParks(),
            info.isNearSchools()
        );
        
        return ResponseEntity.ok(dto);
    }
}
