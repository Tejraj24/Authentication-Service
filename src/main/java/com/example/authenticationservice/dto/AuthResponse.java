package com.example.authenticationservice.dto;

public class AuthResponse {

    private final String token;
    private final String tokenType;
    private final String username;
    private final String role;
    private final long expiresInMinutes;

    public AuthResponse(String token, String tokenType, String username, String role, long expiresInMinutes) {
        this.token = token;
        this.tokenType = tokenType;
        this.username = username;
        this.role = role;
        this.expiresInMinutes = expiresInMinutes;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public long getExpiresInMinutes() {
        return expiresInMinutes;
    }
}