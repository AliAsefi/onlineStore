package com.example.onlineStore.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication manager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Security filter chain to configure endpoint access
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/products/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Logout successful");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); // Keep session

        return http.build();
    }
}

/*
    Security Configuration:
    SecurityConfig class: Configures Spring Security.
    Defines password encoding (e.g., BCryptPasswordEncoder).
    Configures endpoints' access based on roles (Admin, Buyer, Guest, etc.).
    Sets up JWT (optional, but recommended for stateless authentication).
    Enables CORS, CSRF settings, etc.

   Explanation:

    requestMatchers:
    /admin/** → only ADMIN role can access.
    /buyer/** → only BUYER role can access.
    /search/**, /register, /login → open for everyone.
    Others → must be authenticated.

    Form Login:
    Custom login page (you'll create /login endpoint & page later).
    Redirect to / after successful login.

    Logout:
    Redirects to login page after logout.

    CSRF:
    Disabled temporarily to simplify testing (can enable later with CSRF tokens).

    PasswordEncoder:
    BCrypt for secure password hashing.
*/