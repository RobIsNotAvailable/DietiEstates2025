package com.dd25.dietiestates25.service.utilityservice;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dd25.dietiestates25.repository.ListingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListingStatsService 
{
    private final ListingRepository repo;

    @Async
    @Transactional
    public void incrementViews(Integer id) 
    {
        repo.incrementViews(id);
    }
}