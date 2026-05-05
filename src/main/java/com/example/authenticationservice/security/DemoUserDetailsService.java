package com.example.authenticationservice.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DemoUserDetailsService implements UserDetailsService {

    public static class Account {
        private final String username;
        private final String password;
        private final String role;

        public Account(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }

    private final Map<String, Account> users = new HashMap<String, Account>();

    public DemoUserDetailsService() {
        users.put("admin", new Account("admin", "Admin123!", "ADMIN"));
        users.put("user", new Account("user", "User123!", "USER"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = users.get(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.withUsername(account.getUsername())
            .password("{noop}" + account.getPassword())
            .roles(account.getRole())
            .build();
    }

    public Account findAccountByUsername(String username) {
        return users.get(username);
    }
}