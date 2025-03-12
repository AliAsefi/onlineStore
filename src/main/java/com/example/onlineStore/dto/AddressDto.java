package com.example.onlineStore.dto;

import com.example.onlineStore.entity.OrderEntity;
import com.example.onlineStore.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.LifecycleState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;
    private Long userId;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private List<OrderDto> orderlist = new ArrayList<>();
}
