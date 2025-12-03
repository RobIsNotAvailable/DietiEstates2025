package com.DD25.DietiEstates25.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DD25.DietiEstates25.Repository.ClientRepository;

@RestController
public class ClientController {

    private final ClientRepository repo;

    public ClientController(ClientRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/test")
    public long test() {
        return repo.count();
    }
}
