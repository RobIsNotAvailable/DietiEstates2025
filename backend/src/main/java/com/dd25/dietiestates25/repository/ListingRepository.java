package com.dd25.dietiestates25.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dd25.dietiestates25.model.Listing;

import jakarta.transaction.Transactional;

public interface ListingRepository extends JpaRepository<Listing, Integer>, JpaSpecificationExecutor<Listing>
{
    @Modifying
    @Transactional
    @Query("UPDATE Listing l SET l.views = l.views + 1 WHERE l.id = :id")
    void incrementViews(@Param("id") Integer id);
}
