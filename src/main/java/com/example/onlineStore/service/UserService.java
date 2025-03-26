package com.example.onlineStore.service;

import com.example.onlineStore.dto.AddressDto;
import com.example.onlineStore.dto.RegisterDto;
import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.AddressEntity;
import com.example.onlineStore.entity.RoleEntity;
import com.example.onlineStore.entity.UserEntity;
import com.example.onlineStore.enums.RoleName;
import com.example.onlineStore.mapper.UserMapper;
import com.example.onlineStore.repository.RoleRepository;
import com.example.onlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto registerUser(RegisterDto registerDto){

        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        // Set default role USER
        RoleEntity defaultRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        // Create the user entity from RegisterDto
        UserEntity userEntity = new UserEntity();

        userEntity.setFirstName(registerDto.getFirstName());
        userEntity.setLastName(registerDto.getLastName());
        userEntity.setUsername(registerDto.getUsername());
        userEntity.setEmail(registerDto.getEmail());
        userEntity.setPassword(encodedPassword);
        userEntity.setPhone(registerDto.getPhone());
        userEntity.setRoles(Collections.singleton(defaultRole));  // Set default role
        userEntity.setBirthDate(registerDto.getBirthDate());
        userEntity.setGender(registerDto.getGender());

        // Add the user's addresses from AddressDto
        List<AddressEntity> addressEntities = new ArrayList<>();

        for (AddressDto addressDto : registerDto.getAddressList()){
            AddressEntity addressEntity = new AddressEntity();

            addressEntity.setState(addressDto.getState());
            addressEntity.setAddress(addressDto.getAddress());
            addressEntity.setCountry(addressDto.getCountry());
            addressEntity.setCity(addressDto.getCity());
            addressEntity.setPostalCode(addressDto.getPostalCode());
            addressEntity.setUser(userEntity);

            addressEntities.add(addressEntity);
        }

        //Set the addresses to the user
        userEntity.setAddressList(addressEntities);

        userRepository.save(userEntity);
        return userMapper.mapUserEntityToDto(userEntity);
    }

    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream()
                .map(userMapper::mapUserEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Pageable: Explanation under the Class
     */
    public Page<UserDto> getPageAllUsers(Pageable pageable){
        Page<UserEntity> userEntityPage = userRepository.findAll(pageable);
        return userEntityPage.map(userMapper::mapUserEntityToDto);
    }

    public UserDto getUserById(Long id){
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        return userMapper.mapUserEntityToDto(userEntityOptional.orElseThrow(() -> new RuntimeException("User not found with id: " + id)));
    }

    public UserDto getUserByUsername(String username){
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return userMapper.mapUserEntityToDto(userEntity);
    }

    public UserDto updateUser(Long id, UserDto userDto){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found!"));

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setGender(userDto.getGender());
        userEntity.setBirthDate(userDto.getBirthDate());
        userEntity.setPhone(userDto.getPhone());
        userEntity.setEmail(userDto.getEmail());

        // ✅ Handle password update safely
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            userEntity.setPassword(userDto.getPassword()); // 🔴 Consider hashing if needed
        }

        userRepository.save(userEntity);
        return userMapper.mapUserEntityToDto(userEntity);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }


//    // Method to hash the password
//    public String hashPassword(String plainPassword) {
//        // The strength is a number between 4 and 31; higher values provide stronger security
//        // The following line hashes the password
//        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));  // 12 is the log rounds
//    }
//
//    // Method to verify if the password matches the hash
//    public boolean verifyPassword(String plainPassword, String hashedPassword) {
//        return BCrypt.checkpw(plainPassword, hashedPassword);
//    }
}

/*
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::mapUserEntityToDto);
    }

    Explanation:
    Page<UserEntity> is returned from the repository.
    .map(userMapper::mapUserEntityToDto) converts UserEntity to UserDto.

    Controller:
    @GetMapping
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    Spring automatically parses pagination parameters (page, size, sort) from the request.

    Default Pagination (First Page, 10 Users per Page)
    GET http://localhost:8080/users

     Custom Page & Size
     GET http://localhost:8080/users?page=1&size=5
     Gets page 1 (second page), 5 users per page.

    Sorting (Ascending & Descending)
    GET http://localhost:8080/users?page=0&size=5&sort=firstName,asc
    GET http://localhost:8080/users?page=0&size=5&sort=lastName,desc
    Sorts by firstName (ascending) or lastName (descending).

------------------------------------------------------------------------------
    Advanced: Customizing Pageable Defaults
    If you want default values (e.g., 20 users per page instead of 10), you can define a Pageable bean:

    @Configuration
    public class PaginationConfig {

        @Bean
        public Pageable defaultPageable() {
            return PageRequest.of(0, 20); // Default: page 0, 20 users per page
        }
    }

 */