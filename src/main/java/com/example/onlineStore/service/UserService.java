package com.example.onlineStore.service;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.UserEntity;
import com.example.onlineStore.mapper.UserMapper;
import com.example.onlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserDto createUser(UserDto userDto){
        UserEntity userEntity = userMapper.mapUserDtoToEntity(userDto);
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