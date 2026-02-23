package com.dd25.dietiestates25.repository;
//the class might be moved to a dedicated package in future

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.model.enums.ListingType;

import jakarta.persistence.criteria.Path;

public class ListingSpecs 
{

    private ListingSpecs() {}
    
    public static Specification<Listing> hasCity(String city) 
    {
        return (root, query, cb) -> 
        {
            if (city == null || city.isBlank()) 
                return cb.conjunction();

            return cb.equal(root.get("city"), city);
        };
    }

    public static Specification<Listing> hasPriceRange(BigDecimal min, BigDecimal max) 
    {
        return (root, query, cb) -> 
        {
            Path<BigDecimal> pricePath = root.get("price");
            
            if (min != null && max != null) return cb.between(pricePath, min, max);
            if (min != null) return cb.greaterThanOrEqualTo(pricePath, min);
            if (max != null) return cb.lessThanOrEqualTo(pricePath, max);
            return cb.conjunction();
        };
    }

    public static Specification<Listing> hasMinRooms(int minRooms)
    {
        return (root, query, cb) -> 
        {
            if (minRooms <= 0) 
                return cb.conjunction();

            return cb.greaterThanOrEqualTo(root.get("numberOfRooms"), minRooms);
        };
    }


    public static Specification<Listing> hasEnergyClass(String energyClass)
    {
        return (root, query, cb) -> 
        {
            if (energyClass == null || energyClass.isBlank()) 
                return cb.conjunction();

            return cb.equal(root.get("energyClass"), energyClass);
        };
    }

    public static Specification<Listing> hasListingType(ListingType listingType)
    {
        return (root, query, cb) -> 
        {
            if (listingType == null) 
                return cb.conjunction();

            return cb.equal(root.get("listingType"), listingType);
        };
    }

    public static Specification<Listing> isActive(boolean active) 
    {
        return (root, query, cb) -> cb.equal(root.get("active"), active);
    }
}