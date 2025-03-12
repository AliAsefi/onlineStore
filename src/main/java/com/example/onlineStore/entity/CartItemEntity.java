package com.example.onlineStore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer quantity;
    private double pricePerUnit; //comes from ProductEntity
    private double discountPercentage; //Can be modified before purchase
    private double totalPrice; //Calculated field

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemEntity cartItemEntity = (CartItemEntity) o;
        return Objects.equals(id, cartItemEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

/*
    Why Do We Need OrderItem?
    Problem Without OrderItem
    An order contains multiple products, but each product may have a different quantity in an order.
    If we use direct Many-to-Many (@ManyToMany between Order and Product), we lose information about the quantity.
    We also can’t store additional attributes like discount per item.

    When a user places an order, OrderItem stores:

    Which product was ordered.
    How many units were ordered.
    The price at purchase time (in case prices change later).
    The discount applied to this specific item.
    This structure ensures: ✅ Accurate price tracking (even if product prices change later).
    ✅ Flexible discounts (some items can have discounts, some not).
    ✅ Easier calculations (just sum totalPrice in OrderItem).

---------------------------------------------------------------------------
    private double pricePerUnit;
    private Double discountPercentage;

    Yes, you should keep both pricePerUnit and discountPercentage in the OrderItemEntity. Here's why:

    1. pricePerUnit:
    Why keep it?
    The pricePerUnit represents the price of the product at the time the order is made. This is important because product prices can change over time (due to updates, promotions, etc.), but you want to preserve the price at the time of the order.

    Scenario:
    If the price of the product changes after the order is placed, storing the pricePerUnit in the OrderItemEntity ensures that the price paid by the user remains the same, even if the price of the product changes later.

    Example:
    Imagine a user orders a product when its price is $100. Later, the product price increases to $120. If the pricePerUnit is stored in the OrderItemEntity, you ensure that the user will still pay the original price of $100, regardless of future price changes.

    2. discountPercentage:
    Why keep it?
    The discountPercentage indicates any discount applied to the product at the time of purchase. This is important because the discount can vary based on sales, promotions, or user-specific discounts (like coupons or memberships).

    Scenario:
    If you apply a discount to a product, you want to preserve this information in the OrderItemEntity so that the discount can be tracked as part of the order. Without saving this information, you would lose track of how much of a discount the user received for that specific order.

    Example:
    If a product is originally $100 and a 10% discount is applied, the user pays $90. Storing the discountPercentage ensures that you can track that the discount was applied correctly and calculate the original price, total price, and potential refunds if needed.

    What would happen if you didn't store these values?
    If you did not store pricePerUnit and discountPercentage in the OrderItemEntity, you would lose this historical information and could only retrieve the current price and discount of the product (which could be different than what the user originally paid). This might lead to inaccurate billing, auditing issues, and difficulty when trying to calculate refunds or returns.
    When NOT to store them:
    If the price and discount are always fixed and never change (which is rare in real-world applications), you might not need to store them separately. However, this is a rare case. In most business models, prices and discounts can fluctuate, and hence it's important to store them at the time the order is placed.

----------------------------------------------------------
 */