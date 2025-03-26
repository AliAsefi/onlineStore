package com.example.onlineStore.controller;

import com.example.onlineStore.dto.JwtResponse;
import com.example.onlineStore.dto.LoginDto;
import com.example.onlineStore.dto.RegisterDto;
import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.RoleEntity;
import com.example.onlineStore.entity.UserEntity;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.security.JwtTokenUtil;
import com.example.onlineStore.service.CustomUserDetailsService;
import com.example.onlineStore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterDto registerDto){
        return new ResponseEntity<>(userService.registerUser(registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            // Generate JWT token
            String token = jwtTokenUtil.generateToken(loginDto.getUsername());


            // Fetch user details
            UserDto userDto = userService.getUserByUsername(loginDto.getUsername());

            return ResponseEntity.ok(new JwtResponse(token, userDto));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear authentication
        SecurityContextHolder.clearContext();

        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Logout successful"));
    }
}
