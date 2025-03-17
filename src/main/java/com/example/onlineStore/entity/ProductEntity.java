package com.example.onlineStore.entity;

import com.example.onlineStore.enums.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String description;  // Optional product description
    private String image; //file path
    private int stockQuantity;
    private double price;
    private double discountPercentage;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    /**
     * This means one product can appear in many cart items.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CartItemEntity> cartItemList;

    @JsonIgnore
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderItemEntity> orderItemList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity productEntity = (ProductEntity) o;
        return Objects.equals(id, productEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

/*
    Use @JsonIgnore to prevent infinite loops in JSON serialization.
 */