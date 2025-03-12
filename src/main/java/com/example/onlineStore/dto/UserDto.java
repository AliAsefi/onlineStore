package com.example.onlineStore.dto;

import com.example.onlineStore.entity.OrderEntity;
import com.example.onlineStore.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String password;

    private CartDto cartDto;
    private List<AddressDto> addressList = new ArrayList<>();
    private List<OrderDto> orderlist = new ArrayList<>();
}

/*
    EnumType.STRING stores values like "MALE", "FEMALE" in the database.
    Better than EnumType.ORDINAL, which stores numbers (0, 1, 2), making the DB less readable.


    User - Address (One-to-Many)
    A user can have multiple addresses (home, work, etc.).
    One address belongs to only one user.


    User - Order (One-to-Many)
    A user can place multiple orders.
    Each order belongs to one user.

 */