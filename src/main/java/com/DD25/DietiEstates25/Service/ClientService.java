package com.DD25.DietiEstates25.Service;

import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DD25.DietiEstates25.Model.Client;
import com.DD25.DietiEstates25.Repository.ClientRepository;

@Service
public class ClientService 
{
    private final ClientRepository repo;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public ClientService(ClientRepository repo)
    {
        this.repo = repo;
    }

    public void registerClient(@NonNull String email, String firstName, String lastName, String rawPassword)
    {
        if (!rawPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
        {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain both letters and numbers");
        }

        Client client = new Client(email, firstName, lastName, encoder.encode(rawPassword));

        if (repo.findById(email).isPresent())
        {
            throw new IllegalStateException("Email already registered");
        }

        repo.save(client);
    }

    public void login(@NonNull String email, String rawPassword)
    {
        Optional<Client> clientOptional = repo.findById(email);

        if (clientOptional.isPresent())
        {
            Client client = clientOptional.get();
            if (!encoder.matches(rawPassword, client.getHashPassword()))
            {
                throw new SecurityException("Invalid credentials");
            }
        }
        else
        {
            throw new SecurityException("Invalid credentials");
        }
    }
}
