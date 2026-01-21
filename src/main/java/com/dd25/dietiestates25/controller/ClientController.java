package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.dd25.dietiestates25.service.ClientService;

record ChangePasswordRequest(String oldPassword, String newPassword) {} // might be moved elsewhere later

@RestController
@RequestMapping("/api/clients")
public class ClientController 
{
    private final ClientService service;

    public ClientController(ClientService service) {
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

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam @NonNull String email, @RequestBody ChangePasswordRequest request) 
    {
        service.changePassword(email, request.oldPassword(), request.newPassword());
        return ResponseEntity.ok("Password succesfully updated");
    }
}