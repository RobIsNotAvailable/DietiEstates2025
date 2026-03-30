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
    private static final double SEARCH_RADIUS_KM = 2.0;
    private static final String SURROUNDING_INFO = "surroundingInfo";
    private static final String HOUSE_INFO = "houseInfo";
    private static final String BUILDING_DETAILS = "buildingDetails";
    private static final String ADDRESS = "address";

    private ListingSpecs() {}
    
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

    public static Specification<Listing> hasMinRooms(Integer minRooms)
    {
        return (root, query, cb) -> 
        {
            if (minRooms == null || minRooms <= 0) 
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
    
    public static Specification<Listing> hasNearSchools(Boolean nearSchools) 
    {
        return (root, query, cb) -> 
            (nearSchools == null || !nearSchools) ? null : cb.isTrue(root.get(SURROUNDING_INFO).get("nearSchools"));
    }

    public static Specification<Listing> hasNearStops(Boolean nearStops) 
    {
        return (root, query, cb) -> 
            (nearStops == null || !nearStops) ? null : cb.isTrue(root.get(SURROUNDING_INFO).get("nearStops"));
    }

    public static Specification<Listing> hasNearParks(Boolean nearParks) 
    {
        return (root, query, cb) -> 
            (nearParks == null || !nearParks) ? null : cb.isTrue(root.get(SURROUNDING_INFO).get("nearParks"));
    }

    public static Specification<Listing> hasLocation(String city, Double lat, Double lon) 
    {
        return (root, query, cb) -> 
        {
            if (lat != null && lon != null) 
            {
                double latThreshold = SEARCH_RADIUS_KM / 111.0;
                double lonThreshold = SEARCH_RADIUS_KM / (111.0 * Math.cos(Math.toRadians(lat)));

                Path<Double> latPath = root.get(HOUSE_INFO).get(BUILDING_DETAILS).get(ADDRESS).get("coordinates").get("latitude");
                Path<Double> lonPath = root.get(HOUSE_INFO).get(BUILDING_DETAILS).get(ADDRESS).get("coordinates").get("longitude");

                return cb.and(
                    cb.between(latPath, lat - latThreshold, lat + latThreshold),
                    cb.between(lonPath, lon - lonThreshold, lon + lonThreshold)
                );
            }

            if (city != null && !city.isBlank()) 
            {
                return cb.equal(cb.lower(getCityPath(root)), city.toLowerCase());
            }

            return cb.conjunction(); 
        };
    }


    public static Specification<Listing> isActive() 
    {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("status"), com.dd25.dietiestates25.model.enums.Status.ACTIVE);
    }

    private static Path<String> getCityPath(Root<Listing> root) 
    {
        return root.get(HOUSE_INFO)
                .get(BUILDING_DETAILS)
                .get(ADDRESS)
                .get("postalAddress")
                .get("city");
    }

    private static Path<BigDecimal> getPricePath(Root<Listing> root) 
    {
        return root.get("commercialInfo")
                .get("price");
    }

    private static Path<Integer> getRoomsPath(Root<Listing> root) 
    {
        return root.get(HOUSE_INFO)
                .get("houseDetails")
                .get("numberOfRooms");
    }

    private static Path<String> getEnergyClassPath(Root<Listing> root)
    {
        return root.get(HOUSE_INFO)
                .get("houseDetails")
                .get("energyClass");
    }

    private static Path<ListingType> getListingTypePath(Root<Listing> root)
    {
        return root.get("commercialInfo")
                .get("listingType");
    }

}