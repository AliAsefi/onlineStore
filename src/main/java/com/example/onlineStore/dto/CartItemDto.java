package com.example.onlineStore.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private Long id;
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private double pricePerUnit; //comes from ProductEntity
    private double discountPercentage; //Can be modified before purchase
    private double totalPrice; //Calculated field
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

 -------------------------------------------------------------------------------------

    The discountPercentage should typically come from the ProductEntity. However, there are cases where a discount might be modified before purchase, such as:

    Special Offers – A seller might apply an additional discount at checkout.
    Bulk Discounts – If the user buys more than a certain quantity, they might get a better discount.
    Coupon Codes – Users might enter a promo code that affects the final discount.
 */