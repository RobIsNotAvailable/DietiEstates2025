package com.DD25.DietiDeals25;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ControllerTest {
    @GetMapping("/")
    public String test() {
        return "test";
    }
}
