package com.dd25.dietiestates25.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.dd25.dietiestates25.dto.GeoapifyProperties;
import com.dd25.dietiestates25.dto.GeoapifyResponse;
import com.dd25.dietiestates25.model.Address;

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

        if (response == null || response.getFeatures() == null || response.getFeatures().isEmpty()) {
            throw new RuntimeException("Indirizzo non trovato o risposta non valida");
        }

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
        address.setLatitude(props.getLat());
        address.setLongitude(props.getLon());
        address.setPlaceId(props.getPlaceId());
        address.setFormattedAddress(props.getFormatted());
        
        return address;
    }
}

