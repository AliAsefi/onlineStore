//package com.example.onlineStore.config;
//
//import com.example.onlineStore.entity.RoleEntity;
//import com.example.onlineStore.enums.RoleName;
//import com.example.onlineStore.repository.RoleRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DatabaseInitializer {
//
//    @Bean
//    CommandLineRunner initRoles(RoleRepository roleRepository) {
//        return args -> {
//            if (roleRepository.findByRoleName(RoleName.ROLE_USER).isEmpty()) {
//                RoleEntity userRole = new RoleEntity();
//                userRole.setRoleName(RoleName.ROLE_USER);
//                roleRepository.save(userRole);
//            }
//            if (roleRepository.findByRoleName(RoleName.ROLE_ADMIN).isEmpty()) {
//                RoleEntity adminRole = new RoleEntity();
//                adminRole.setRoleName(RoleName.ROLE_ADMIN);
//                roleRepository.save(adminRole);
//            }
//            if (roleRepository.findByRoleName(RoleName.ROLE_GUEST).isEmpty()) {
//                RoleEntity guestRole = new RoleEntity();
//                guestRole.setRoleName(RoleName.ROLE_GUEST);
//                roleRepository.save(guestRole);
//            }
//            System.out.println("Roles initialized!");
//        };
//    }
//}
//
////You only need to initialize the roles once when setting up your database or when there are changes to the roles.