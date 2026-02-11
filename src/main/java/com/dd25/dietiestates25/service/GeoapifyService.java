package com.dd25.dietiestates25.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.dd25.dietiestates25.dto.GeoapifyProperties;
import com.dd25.dietiestates25.dto.GeoapifyResponse;
import com.dd25.dietiestates25.dto.GeoapifyResponse.GeoapifyFeature;
import com.dd25.dietiestates25.model.Address;
import com.dd25.dietiestates25.model.SurroundingInfo;

@Service 
public class GeoapifyService 
{

    @Value("${geoapify.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GeoapifyService(RestTemplate restTemplate) 
    {
        this.restTemplate = restTemplate;
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

        if (response == null || response.getFeatures() == null || response.getFeatures().isEmpty()) 
            throw new IllegalArgumentException("invalid response from given address: " + rawAddress);
        
        GeoapifyProperties props = response.getFeatures().get(0).getProperties();

        return mapToAddress(props);
    }

    private Address mapToAddress(GeoapifyProperties props) 
    {
        Address address = new Address();

        address.setCity(props.getCity());
        address.setStreet(props.getStreet());
        address.setHouseNumber(props.getHousenumber());
        address.setProvince(props.getState());
        address.setZipCode(props.getPostcode());
        address.setCountry(props.getCountry());
        address.setPlaceId(props.getPlaceId());
        address.setFormattedAddress(props.getFormatted());
        address.setLatitude(props.getLat());
        address.setLongitude(props.getLon());        
        
        return address;
    }

    public SurroundingInfo fetchSurroundingInfo(double lat, double lon) 
    {
        String url = buildUrl(lat, lon, "education.school,building.school,building.kindergarten,leisure.park,public_transport", 50);
        
        Objects.requireNonNull(url, "URL cannot be null");

        if (url.isEmpty()) 
            throw new IllegalArgumentException("URL cannot be empty");

        GeoapifyResponse response = restTemplate.getForObject(url, GeoapifyResponse.class);
        
        boolean nearSchools = isCategoryPresent(response, "school") || isCategoryPresent(response, "kindergarten");
        boolean nearParks = isCategoryPresent(response, "park");
        boolean nearStops = isCategoryPresent(response, "transport");

        return new SurroundingInfo(nearStops, nearParks, nearSchools);
    }

    private String buildUrl(double lat, double lon, String categories, int limit) 
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
        if (res == null || res.getFeatures() == null) 
            return false;

        String lowerKeyword = keyword.toLowerCase();

        return res.getFeatures().stream()
                .filter(Objects::nonNull)
                .map(GeoapifyFeature::getProperties) 
                .filter(Objects::nonNull)
                .map(GeoapifyProperties::getCategories) 
                .filter(Objects::nonNull)
                .anyMatch(categories -> categories.stream()
                        .filter(Objects::nonNull)
                        .anyMatch(c -> c.toLowerCase().contains(lowerKeyword)));
    }
}

