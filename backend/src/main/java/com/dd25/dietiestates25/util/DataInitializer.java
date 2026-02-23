package com.dd25.dietiestates25.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;

@Component
public class DataInitializer implements CommandLineRunner
{

    @Value("${ADMIN_PW}")
    private String adminPw;
    private final PasswordEncoder encoder;
    
    private final CompanyAccountRepository accountRepo;
    // e tutte le altre repo per popolare

    public DataInitializer(CompanyAccountRepository accountRepo, PasswordEncoder encoder)
    {
        this.accountRepo = accountRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args)
    {
        createAdminIfMissing();
    }

    private void createAdminIfMissing() {
        if (accountRepo.findById("admin@dietiestates.com").isEmpty())
        {
            CompanyAccount admin = new CompanyAccount(
                "admin@dietiestates.com", 
                "admin", 
                "admin", 
                encoder.encode(adminPw),
                SecurityLevel.ADMIN);
            accountRepo.save(admin);
        }
    }
}