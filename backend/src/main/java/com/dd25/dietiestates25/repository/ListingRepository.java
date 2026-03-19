package com.dd25.dietiestates25.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dd25.dietiestates25.dto.response.ListingStatsResponse;
import com.dd25.dietiestates25.model.Listing;

import jakarta.transaction.Transactional;

public interface ListingRepository extends JpaRepository<Listing, Integer>, JpaSpecificationExecutor<Listing>
{
    @Modifying
    @Transactional
    @Query("UPDATE Listing l SET l.views = l.views + 1 WHERE l.id = :id")
    void incrementViews(@Param("id") Integer id);

    @Query("SELECT l FROM Listing l WHERE l.agent.email = :agentEmail")
    List<Listing> findByAgent(@Param("agentEmail") String agentEmail);
    
    @Query
    ("""
        SELECT new com.dd25.dietiestates25.dto.response.ListingStatsResponse
        (
            l.id, 
            l.name, 
            ci.price, 
            CAST(ci.listingType AS string), 
            hi.buildingDetails.address.geocodingDetails.formattedAddress, 
            p.filepath, 
            l.views, 
            CAST(COUNT(DISTINCT vr.id) AS int), 
            CAST(COUNT(DISTINCT o.id) AS int), 
            MAX(o.proposedPrice), 
            l.lastModified, 
            l.status,
            (SELECT o2.proposedPrice FROM Offer o2 
            WHERE o2.listing.id = l.id AND o2.status = com.dd25.dietiestates25.model.enums.Status.ENDED_SUCCESFULLY 
            ORDER BY o2.id DESC LIMIT 1)
        )
        FROM Listing l
        JOIN l.commercialInfo ci
        JOIN l.houseInfo hi
        LEFT JOIN Photo p ON p.listing.id = l.id AND p.position = 1
        LEFT JOIN VisitRequest vr ON vr.listing.id = l.id
        LEFT JOIN Offer o ON o.listing.id = l.id
        WHERE l.id = :id
        GROUP BY l.id, l.name, ci.price, ci.listingType, 
                hi.buildingDetails.address.geocodingDetails.formattedAddress, 
                p.filepath, l.views, l.lastModified, l.status
    """)
    ListingStatsResponse getStatsByListing(@Param("id") Integer id);

    @Query
    (
        """
        SELECT COUNT(l) = 0 
        FROM Listing l 
        JOIN l.houseInfo h 
        JOIN h.buildingDetails bd 
        JOIN bd.address a 
        WHERE a.geocodingDetails.placeId = :placeId 
        AND bd.intern = :intern 
        AND l.status = 'ACTIVE'
        """
    )
    boolean isAddressAvailable(@Param("placeId") String placeId, @Param("intern") int intern);

    @Query
    (
        value = """
        SELECT DISTINCT l FROM Listing l
        LEFT JOIN FETCH l.commercialInfo
        LEFT JOIN FETCH l.houseInfo hi
        LEFT JOIN FETCH hi.buildingDetails.address
        LEFT JOIN FETCH l.surroundingInfo
        LEFT JOIN FETCH l.photos
        WHERE l.status = com.dd25.dietiestates25.model.enums.Status.ACTIVE
        """,
        countQuery = """
        SELECT COUNT(l) FROM Listing l
        WHERE l.status = com.dd25.dietiestates25.model.enums.Status.ACTIVE
        """
    )
    Page<Listing> findAllActiveWithDetails(Pageable pageable);
}