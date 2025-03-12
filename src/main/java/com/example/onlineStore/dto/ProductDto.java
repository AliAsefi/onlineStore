package com.example.onlineStore.dto;

import com.example.onlineStore.entity.OrderItemEntity;
import com.example.onlineStore.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.query.Order;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private String description;  // Optional product description
    private String image; //file path
    private int stockQuantity;
    private double price;
    private double discountPercentage;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    private List<CartItemDto> cartItemList = new ArrayList<>();
    private List<OrderItemDto> orderItemList = new ArrayList<>();
}
