package com.dd25.dietiestates25.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyResponse 
{
    private List<GeoapifyFeature> features;

    public List<GeoapifyFeature> getFeatures() { return features; }
    
    public void setFeatures(List<GeoapifyFeature> features) { this.features = features; }

    public static class GeoapifyFeature 
    {
        private GeoapifyProperties properties;

        public GeoapifyProperties getProperties() { return properties; }
        public void setProperties(GeoapifyProperties properties) { this.properties = properties; }
    }
}