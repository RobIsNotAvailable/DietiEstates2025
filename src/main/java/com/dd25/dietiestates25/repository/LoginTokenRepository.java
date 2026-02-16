package com.dd25.dietiestates25.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dd25.dietiestates25.model.LoginToken;

public interface LoginTokenRepository extends JpaRepository<LoginToken, String>{}
