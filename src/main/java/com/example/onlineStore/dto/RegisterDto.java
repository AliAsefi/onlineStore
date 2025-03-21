package com.example.onlineStore.dto;

import com.example.onlineStore.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {

    private String firstName;
    private String lastName;
    private String username;
    @Enumerated(EnumType.STRING)
    private Gender gender; //MALE, FEMALE, OTHER
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String password;

    private List<AddressDto> addressList = new ArrayList<>();
}