package com.example.onlineStore.service;

import com.example.onlineStore.entity.UserEntity;
import com.example.onlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}

/*
    1. Implementing UserDetailsService
    The UserDetailsService interface is a core component in Spring Security, responsible for retrieving user-related data.
    It has a single method, loadUserByUsername(), which locates the user based on the username.
    This method is crucial for authentication and authorization processes.

    Steps:
    Create a class that implements UserDetailsService.
    Override the loadUserByUsername() method to fetch user details from the database.
    Convert your UserEntity to a UserDetails object.

    Explanation:
    CustomUserDetailsService: This service implements UserDetailsService and provides the logic to fetch user details from the database.
    loadUserByUsername(): Fetches the user by email and converts the user's roles into authorities required by Spring Security.

    Spring Boot does this automatically if:
    There’s a UserDetailsService bean (@Service + implements UserDetailsService ✅ You have it!)
    AuthenticationManager bean is defined (✅ You have it!)
    👉 That’s why your CustomUserDetailsService is already in use without extra config.

    How it works during login:
    User submits username & password (/login endpoint).
    AuthenticationManager → triggers Spring Security flow.
    Spring Security calls loadUserByUsername() from CustomUserDetailsService to load user data.
    Password is checked (using password encoder).
    Roles (authorities) and password are used to authenticate.
    So you ARE using it — but Spring Security handles calling it behind the scenes! 🎯
 */