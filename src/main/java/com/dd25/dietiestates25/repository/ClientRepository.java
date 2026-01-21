package com.dd25.dietiestates25.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dd25.dietiestates25.model.Client;

public interface ClientRepository extends JpaRepository<Client, String> {}
