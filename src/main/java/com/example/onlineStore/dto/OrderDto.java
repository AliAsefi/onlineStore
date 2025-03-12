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
    private OrderStatus orderStatus;
    private double totalCartPrice = 0.0; //calculated field
    private List<OrderItemDto> orderItemList = new ArrayList<>();
}

/*
    private Double totalOrderPrice;

    Good question! If you have calculated attributes like totalOrderPrice, calling the calculation method in every service method (create, get, update) feels repetitive. There are a few better ways to handle it efficiently. Let's explore them:

    🔹 Better Ways to Handle Calculated Fields
    ✅ Option 1: Use @PostLoad in Entity (Best for Read Operations)
    Instead of calculating totalOrderPrice manually every time in the service layer, you can use the @PostLoad annotation in your OrderEntity. This will automatically set totalOrderPrice every time the entity is loaded from the database.

    @Getter
    @Setter
    @Entity
    @Table(name = "orders")
    public class OrderEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        private LocalDateTime orderDate;

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItemEntity> orderItemList;

        @Transient // This field is not saved in the database
        private double totalOrderPrice;

        @PostLoad
        @PostPersist
        @PostUpdate
        public void calculateTotalOrderPrice() {
            this.totalOrderPrice = orderItemList.stream()
                    .mapToDouble(OrderItemEntity::getTotalPrice)
                    .sum();
        }
    }
    ✅ How It Works
    @PostLoad: Runs after fetching from the database.
    @PostPersist: Runs after saving a new order.
    @PostUpdate: Runs after updating an order.
    @Transient: Ensures totalOrderPrice is not stored in the database.



    -----------------------------------------------------------------------------------------------------------------------

    Option 2: Use a Custom Getter in DTO
    If you want to keep calculations out of the entity, you can add a getter method in the DTO class instead.

    public class OrderDto {
        private Long id;
        private LocalDateTime orderDate;
        private List<OrderItemDto> orderItemList;

        public double getTotalOrderPrice() {
            return orderItemList.stream()
                    .mapToDouble(OrderItemDto::getTotalPrice)
                    .sum();
        }
    }
    ✅ How It Works
    Whenever orderDto.getTotalOrderPrice() is called, the total is calculated on demand.
    This avoids storing the value in memory unnecessarily.
 */