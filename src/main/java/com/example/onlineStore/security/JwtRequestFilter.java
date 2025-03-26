package com.example.onlineStore.security;

import java.io.IOException;

import com.example.onlineStore.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt == null || jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer Header");
            } else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveSubject(jwt);

                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token"); //Use 401 Unauthorized
                    return; // Stop filter chain if JWT is invalid
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}