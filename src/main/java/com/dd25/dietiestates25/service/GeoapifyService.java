package com.dd25.dietiestates25.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class GeoapifyService 
{
    @Value("${geoapify.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GeoapifyService(RestTemplate restTemplate) 
    {
        this.restTemplate = restTemplate;
    }
}
