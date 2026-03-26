package com.dd25.dietiestates25.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dd25.dietiestates25.dto.response.AgentMonthlyStatsResponse;
import com.dd25.dietiestates25.model.CompanyAccount;

public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, String> 
{
    @Query("""
    SELECT new com.dd25.dietiestates25.dto.response.AgentMonthlyStatsResponse
    (
        CAST(SUM(CASE WHEN l.status = 'ACTIVE' THEN 1 ELSE 0 END) AS int),
        
        CAST(SUM(CASE WHEN l.status = 'ENDED_SUCCESFULLY' 
                         AND l.lastModified >= :startOfMonth 
                         AND l.lastModified < :endOfMonth THEN 1 ELSE 0 END) AS int),
                         
        CAST(SUM(CASE WHEN l.status = 'ACTIVE' THEN l.views ELSE 0 END) AS int),

        (SELECT CAST(COALESCE(COUNT(v.id), 0) AS int) 
         FROM VisitRequest v 
         WHERE v.agent.email = :email 
           AND v.status = 'ACTIVE' 
           AND v.visitDate >= :startOfMonth 
           AND v.visitDate < :endOfMonth),

        (SELECT CAST(COALESCE(COUNT(v.id), 0) AS int) 
         FROM VisitRequest v 
         WHERE v.agent.email = :email 
           AND (v.status = 'CONCLUDED' OR v.status = 'ENDED_SUCCESFULLY')
           AND v.visitDate >= :startOfMonth 
           AND v.visitDate < :endOfMonth),

        (SELECT CAST(COALESCE(COUNT(o.id), 0) AS int) 
         FROM Offer o 
         WHERE o.agent.email = :email 
           AND o.expirationDate >= :startOfMonth 
           AND o.expirationDate < :endOfMonth)
    )
    FROM Listing l
    WHERE l.agent.email = :email
""")
AgentMonthlyStatsResponse getStatsForSelectedMonth( 
    @Param("email") String email,
    @Param("startOfMonth") OffsetDateTime startOfMonth,
    @Param("endOfMonth") OffsetDateTime endOfMonth
);
}