package com.DD25.DietiEstates25.Service;

import org.springframework.dao.DataIntegrityViolationException;
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

    public void registerClient(String email, String firstName, String lastName, String rawPassword)
    {
        Client client = new Client(email, firstName, lastName, encoder.encode(rawPassword));
        try 
        {
            System.out.println(client.getEmail() + " " + client.getFirstName() + " " + client.getLastName() + " " + client.getHashPassword());
            repo.save(client);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new IllegalArgumentException("Client with this email already exists", e);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to register client", e);
        }
    }

    public boolean login(String email, String rawPassword)
    {
        Client client = repo.findById(email);
        return client != null && encoder.matches(rawPassword, client.getHashPassword());
    }
}
