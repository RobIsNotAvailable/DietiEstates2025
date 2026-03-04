package com.dd25.dietiestates25.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dd25.dietiestates25.dto.AgentStatsResponse;
import com.dd25.dietiestates25.model.CompanyAccount;

public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, String> 
{
    @Query
    ("""
        SELECT new com.dd25.dietiestates25.dto.AgentStatsResponse
        (
            CAST(SUM(CASE WHEN l.status = 'active' THEN 1 ELSE 0 END) AS int),
            CAST(SUM(CASE WHEN l.status = 'ended_succesfully' THEN 1 ELSE 0 END) AS int)
        )
        FROM Listing l
        WHERE l.agent.email = :email
    """)
    AgentStatsResponse getStatsByAgent(@Param("email") String email);
}