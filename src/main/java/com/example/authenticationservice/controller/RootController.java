package com.example.authenticationservice.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> root() {
        Map<String, String> response = new LinkedHashMap<String, String>();
        response.put("service", "Authentication Service");
        response.put("status", "running");
        response.put("health", "/api/auth/health");
        response.put("login", "/api/auth/login");
        return response;
    }
}