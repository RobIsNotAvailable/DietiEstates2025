package com.DD25.DietiEstates25.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DD25.DietiEstates25.Service.ClientService;

@RestController
public class ClientController
{

    private final ClientService service;

    public ClientController(ClientService service)
    {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam @NonNull String email, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String password)
    {
        service.registerClient(email, firstName, lastName, password);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam @NonNull String email, @RequestParam String password)
    {
        service.login(email, password);
        return ResponseEntity.ok("Login successful");
    }
}
