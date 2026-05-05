package com.example.authenticationservice.controller;

import com.example.authenticationservice.config.JwtProperties;
import com.example.authenticationservice.dto.AuthResponse;
import com.example.authenticationservice.dto.LoginRequest;
import com.example.authenticationservice.dto.MeResponse;
import com.example.authenticationservice.security.DemoUserDetailsService;
import com.example.authenticationservice.security.JwtService;
import com.example.authenticationservice.security.DemoUserDetailsService.Account;
import javax.validation.Valid;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final DemoUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthController(DemoUserDetailsService userDetailsService, JwtService jwtService, JwtProperties jwtProperties) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        String username = normalizeUsername(request.getUsername());
        String password = normalizePassword(request.getPassword());

        Account account = userDetailsService.findAccountByUsername(username);
        if (account == null || !account.getPassword().equals(password)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, "Bearer", userDetails.getUsername(), extractRole(userDetails.getAuthorities()), jwtProperties.getExpirationMinutes());
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new MeResponse(userDetails.getUsername(), extractRole(userDetails.getAuthorities()));
    }

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public String health() {
        return "OK";
    }

    private String extractRole(Iterable<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            if (authority != null && authority.getAuthority() != null) {
                String role = authority.getAuthority();
                return role.startsWith("ROLE_") ? role.substring(5) : role;
            }
        }
        return "USER";
    }

    private String normalizeUsername(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }

    private String normalizePassword(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().replaceAll("\\s+", "");
    }
}