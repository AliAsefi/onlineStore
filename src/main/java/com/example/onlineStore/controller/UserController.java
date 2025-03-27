package com.example.onlineStore.controller;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.UserEntity;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/page")
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable){
        return new ResponseEntity<>(userService.getPageAllUsers(pageable),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserByUsername(Authentication authentication){
        return new ResponseEntity<>(userService.getUserByUsername(authentication.getName()),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id, Authentication authentication){

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No authentication found");
        }

        // Log user details
        System.out.println("Authenticated user: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.updateUser(id,userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
