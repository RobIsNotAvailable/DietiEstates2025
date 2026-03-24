package com.dd25.dietiestates25.service.utilityservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.dd25.dietiestates25.dto.GeoapifyProperties;
import com.dd25.dietiestates25.dto.response.GeoapifyResponse;
import com.dd25.dietiestates25.dto.response.SurroundingInfoResponse;
import com.dd25.dietiestates25.model.Address;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class GeoapifyService
{

    @Value("${geoapify.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;


    public List<GeoapifyProperties> getPossibleAddresses(String rawAddress) 
    {
        try 
        {
            String url = UriComponentsBuilder
                .fromUriString("https://api.geoapify.com/v1/geocode/autocomplete")
                .queryParam("text", rawAddress)
                .queryParam("apiKey", apiKey)
                .queryParam("limit", 5)
                .queryParam("filter", "countrycode:it") 
                .build()
                .toUriString();

            GeoapifyResponse response = restTemplate.getForObject(url, GeoapifyResponse.class);

            return Optional.ofNullable(response)
                    .map(GeoapifyResponse::properties)
                        .orElse(Collections.emptyList());    
        } 
        catch (RestClientException e) 
        {
            return Collections.emptyList();
        }
    }

    public Address normalizeAddress(String rawAddress) 
    {
        String url = UriComponentsBuilder
            .fromUriString("https://api.geoapify.com/v1/geocode/search")
            .queryParam("text", rawAddress)
            .queryParam("apiKey", apiKey)
            .build()
            .toUriString();

        GeoapifyResponse response = restTemplate.getForObject(url, GeoapifyResponse.class);

        GeoapifyProperties props = Optional.ofNullable(response)
            .map(res -> res.properties().get(0))
            .orElseThrow(() -> new IllegalArgumentException("Address not found"));


        return mapToAddress(props);
    }

    private Address mapToAddress(GeoapifyProperties props) 
    {
        Address address = new Address();

        address.setCity(props.city());
        
        if (props.street() != null && !props.street().isBlank()) 
        {
            address.setStreet(props.street());
        } 
        else 
        {
            String fallbackStreet = props.formatted() != null ? props.formatted().split(",")[0] : "Unknown";
            address.setStreet(fallbackStreet);
        }

        address.setHouseNumber(props.houseNumber());
        address.setProvince(props.state());
        address.setZipCode(props.postcode());
        address.setCountry(props.country());
        address.setPlaceId(props.placeId());
        address.setFormattedAddress(props.formatted());
        address.setLatitude(props.lat());
        address.setLongitude(props.lon());        
        
        return address;
    }

    public SurroundingInfoResponse fetchSurroundingInfo(double lat, double lon) 
    {
        String url = buildUrl(lat, lon, "education.school,building.school,building.kindergarten,leisure.park,public_transport", 50);
        
        GeoapifyResponse response = Optional.ofNullable(restTemplate.getForObject(url, GeoapifyResponse.class))
            .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        
        boolean nearSchools = isCategoryPresent(response, "school") || isCategoryPresent(response, "kindergarten");
        boolean nearParks = isCategoryPresent(response, "park");
        boolean nearStops = isCategoryPresent(response, "transport");

        return new SurroundingInfoResponse(nearStops, nearParks, nearSchools);
    }

    private @NonNull String buildUrl(double lat, double lon, String categories, int limit) 
    {
        return UriComponentsBuilder
                .fromUriString("https://api.geoapify.com/v2/places")
                .queryParam("categories", categories)
                .queryParam("filter", String.format("circle:%f,%f,500", lon, lat))
                .queryParam("bias", String.format("proximity:%f,%f", lon, lat)) 
                .queryParam("limit", limit)
                .queryParam("apiKey", apiKey)
                .build().toUriString();
    }

    private boolean isCategoryPresent(GeoapifyResponse res, String keyword) 
    {
        String lowerKeyword = keyword.toLowerCase();

        return res.properties().stream()
                    .flatMap(prop -> prop.categories().stream())
                    .anyMatch(category -> category.toLowerCase().contains(lowerKeyword));
    }

    public Address resolveSearchCriteria(String rawAddress) 
    {
        String url = UriComponentsBuilder
                .fromUriString("https://api.geoapify.com/v1/geocode/search")
                .queryParam("text", rawAddress)
                .queryParam("apiKey", apiKey)
                .queryParam("limit", 1)
                .build()
                .toUriString();

        GeoapifyResponse response = restTemplate.getForObject(url, GeoapifyResponse.class);

        if (response == null || response.properties().isEmpty()) 
        {
            throw new IllegalArgumentException("Location not found");
        }

        GeoapifyProperties props = response.properties().get(0);

        return mapToAddress(props);
    }
}

