package com.example.onlineStore.service;

import com.example.onlineStore.dto.CartItemDto;
import com.example.onlineStore.entity.CartEntity;
import com.example.onlineStore.entity.CartItemEntity;
import com.example.onlineStore.mapper.CartItemMapper;
import com.example.onlineStore.repository.CartItemRepository;
import com.example.onlineStore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.onlineStore.calculation.CalculationUtil.roundToTwoDecimal;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private CartRepository cartRepository;

    public List<CartItemDto> getAllCartItems(){
        return cartItemRepository.findAll()
                .stream()
                .map(cartItemMapper::mapCartItemEntityToDto)
                .collect(Collectors.toList());
    }

    public Page<CartItemDto> getPageAllCartItems(Pageable pageable){
        return cartItemRepository.findAll(pageable)
                .map(cartItemMapper::mapCartItemEntityToDto);
    }

    public CartItemDto getCartItemById(Long id){
        CartItemEntity cartItemEntity = cartItemRepository.findById(id)
                .orElseThrow(()->new RuntimeException("CartItem not found!"));
        return cartItemMapper.mapCartItemEntityToDto(cartItemEntity);
    }

    public void deleteCartItem(Long id){
        CartItemEntity cartItemEntity = cartItemRepository.findById(id)
                .orElseThrow(()->new RuntimeException("cartItem not found"));

        CartEntity cartEntity = cartItemEntity.getCart();

        cartItemRepository.deleteById(id);

        //  update totalPrice after deleting cartItem for remaining cartItem
        double totalCartPrice = cartEntity.getCartItemList()
                .stream()
                .mapToDouble(CartItemEntity::getTotalPrice)
                .sum();
        cartEntity.setTotalCartPrice(roundToTwoDecimal(totalCartPrice));
        cartRepository.save(cartEntity);
    }
}

/*
    Let's start with the OrderItemService, as it's a crucial link between Order and Product.

    Step 1: Understanding OrderItemService Responsibilities

    The OrderItemService should:

    Create an OrderItem (when a user adds a product to an order).
    Update the quantity (e.g., if the user changes how many they want).
    Delete an OrderItem (if the user removes it from the order).
    Ensure stock is updated (reduce stock when creating/updating an order item).
    Prevent orders from exceeding available stock (validation).


    ---------------------------------------------------
    Explanation:
    OrderItemDto:

    Added orderId and productId to link the order item to both the order and the product.
    getTotalPrice() calculates the price based on quantity, unit price, and discount.
    OrderItemEntity:

    The OrderItemEntity class already has order (many-to-one relationship to the OrderEntity) and product (many-to-one relationship to the ProductEntity).
    OrderItemMapper:

    The mapOrderItemDtoToEntity method maps the OrderItemDto to OrderItemEntity. It fetches the product by productId and the order by orderId, and sets the respective references in the entity.
    OrderItemService:

    The createOrderItem method uses the order ID to fetch the OrderEntity and then maps the OrderItemDto to OrderItemEntity. It saves the entity and returns the OrderItemDto.
 */