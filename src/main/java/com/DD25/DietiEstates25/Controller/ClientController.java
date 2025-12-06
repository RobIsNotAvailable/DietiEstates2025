package com.DD25.DietiEstates25.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DD25.DietiEstates25.Service.ClientService;

@RestController
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String password)
    {
        System.out.println("DEBUG RAW PASSWORD: " + password);
        service.registerClient(email, firstName, lastName, password);
        return "Registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password)
    {
        return service.login(email, password) ? "ok" : "fail";
    }
}
