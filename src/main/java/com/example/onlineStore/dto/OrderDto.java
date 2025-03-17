package com.example.onlineStore.dto;

import com.example.onlineStore.enums.OrderStatus;
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
public class OrderDto {

    private Long id;
    private Long userId; // Who placed the order
    private Long addressId; // Where to ship
    private Long paymentId;
    private LocalDate orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //PENDING, PAID, SHIPPED, DELIVERED
    private double totalOrderPrice = 0.0; //calculated field
    private List<OrderItemDto> orderItemList = new ArrayList<>();
}