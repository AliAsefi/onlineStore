package com.example.onlineStore.dto;

import com.example.onlineStore.entity.OrderEntity;
import com.example.onlineStore.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Gender gender; //MALE, FEMALE, OTHER
    private LocalDate birthDate;
    private String phone;
    private String email;

    @JsonIgnore
    private String password;

    private CartDto cart;
    private List<AddressDto> addressList = new ArrayList<>();
    private List<OrderDto> orderlist = new ArrayList<>();
}