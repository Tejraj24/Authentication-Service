package com.example.authenticationservice.dto;

public class MeResponse {

    private final String username;
    private final String role;

    public MeResponse(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}