package com.dd25.dietiestates25.repository;
//the class might be moved to a dedicated package in future

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.dd25.dietiestates25.model.Listing;
import com.dd25.dietiestates25.model.enums.ListingType;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public class ListingSpecs 
{

    private ListingSpecs() {}
    
    public static Specification<Listing> hasCity(String city) 
    {
        return (root, query, cb) -> 
        {
            if (city == null) 
                return null;

            return cb.equal(getCityPath(root), city);
        };
    }

    public static Specification<Listing> hasPriceRange(BigDecimal min, BigDecimal max) 
    {
        return (root, query, cb) -> 
        {
            Path<BigDecimal> pricePath = getPricePath(root);
            
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
            {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(getRoomsPath(root), minRooms);
        };
    }

    public static Specification<Listing> hasEnergyClass(String energyClass)
    {
        return (root, query, cb) -> 
        {
            if (energyClass == null || energyClass.isBlank()) 
            {
                return cb.conjunction();
            }

            return cb.equal(getEnergyClassPath(root), energyClass);
        };
    }

    public static Specification<Listing> hasListingType(ListingType listingType)
    {
        return (root, query, cb) -> 
        {
            if (listingType == null) 
            {
                return cb.conjunction();
            }

            return cb.equal(getListingTypePath(root), listingType);
        };
    }
    
    public static Specification<Listing> isActive() 
    {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("status"), com.dd25.dietiestates25.model.enums.Status.ACTIVE);
    }

    private static Path<String> getCityPath(Root<Listing> root) 
    {
        return root.get("houseInfo")
                .get("buildingDetails")
                .get("address")
                .get("city");
    }

    private static Path<BigDecimal> getPricePath(Root<Listing> root) 
    {
        return root.get("commercialInfo")
                .get("price");
    }

    private static Path<Integer> getRoomsPath(Root<Listing> root) 
    {
        return root.get("houseInfo")
                .get("houseDetails")
                .get("numberOfRooms");
    }

    private static Path<String> getEnergyClassPath(Root<Listing> root)
    {
        return root.get("houseInfo")
                .get("houseDetails")
                .get("energyClass");
    }

    private static Path<ListingType> getListingTypePath(Root<Listing> root)
    {
        return root.get("commercialInfo")
                .get("listingType");
    }

}