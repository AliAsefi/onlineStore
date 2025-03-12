package com.example.onlineStore.service;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.dto.CartItemDto;
import com.example.onlineStore.entity.CartEntity;
import com.example.onlineStore.entity.CartItemEntity;
import com.example.onlineStore.entity.ProductEntity;
import com.example.onlineStore.entity.UserEntity;
import com.example.onlineStore.mapper.CartItemMapper;
import com.example.onlineStore.mapper.CartMapper;
import com.example.onlineStore.repository.CartItemRepository;
import com.example.onlineStore.repository.CartRepository;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public CartDto createCart(Long userId, CartItemDto cartItemDto){

        // Retrieve the product
        ProductEntity productEntity = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(()->new RuntimeException("Product not found!"));

        // Check if the requested quantity is available in stock
        if (cartItemDto.getQuantity() > productEntity.getStockQuantity()) {
            throw new RuntimeException("Not enough stock available for product: " + productEntity.getName());
        }

        //-----------------------------------------------------
        // Find user
        UserEntity userEntity = userRepository.findById(userId).
                orElseThrow(()->new RuntimeException("User not found!"));

        //-----------------------------------------------------
        // Find an open cart or create a new one
        CartEntity cartEntity;
        if (cartItemDto.getCartId() == null){
            cartEntity = new CartEntity();
            cartEntity.setTotalCartPrice(0.0);
            cartEntity.setUser(userEntity);
            cartRepository.save(cartEntity);
        }
        else{
            cartEntity = cartRepository.findById(cartItemDto.getCartId())
                    .orElseThrow(()->new RuntimeException("Cart not found!"));

            // Ensure that the cart belongs to the correct user
            if (!cartEntity.getUser().getId().equals(userId)) {
                throw new RuntimeException("This cart does not belong to the user!");
            }
        }

        //-----------------------------------------------------
        // Check if the product is already in the cart
        CartItemEntity existingCart = cartItemRepository.findByCart_IdAndProduct_Id(cartEntity.getId(), productEntity.getId());

        CartItemEntity cartItemEntity;
        if(existingCart != null){
            // Update existing item
            cartItemEntity = existingCart;
            // Check if the requested quantity is available in stock
            int newCartQuantity = existingCart.getQuantity() + cartItemDto.getQuantity();
            if (newCartQuantity > productEntity.getStockQuantity()) {
                throw new RuntimeException("Not enough stock available for product: " + productEntity.getName());
            }
            existingCart.setQuantity(cartItemEntity.getQuantity()+ cartItemDto.getQuantity());
            cartItemEntity.setTotalPrice(existingCart.getTotalPrice());

        }else{
            // Create new cart item
            cartItemEntity = new CartItemEntity();
            cartItemEntity.setProduct(productEntity);
            cartItemEntity.setCart(cartEntity);
            cartItemEntity.setQuantity(cartItemDto.getQuantity());
            cartItemEntity.setPricePerUnit(productEntity.getPrice());
            cartItemEntity.setDiscountPercentage(productEntity.getDiscountPercentage());
        }

        //calculate total cart price
        double totalPrice = cartItemEntity.getQuantity() * productEntity.getPrice() * (1 - productEntity.getDiscountPercentage()/100);
        cartItemEntity.setTotalPrice(totalPrice);

        cartItemRepository.save(cartItemEntity);

        //-----------------------------------------------------
        // Add the cartItemEntity to the cartItemList
        cartEntity.getCartItemList().add(cartItemEntity);
        double totalCartPrice = cartEntity.getCartItemList()
                .stream()
                .mapToDouble(CartItemEntity::getTotalPrice)
                .sum();

        // Update cart total price
        cartEntity.setTotalCartPrice(totalCartPrice);

        cartRepository.save(cartEntity);

        return cartMapper.mapCartEntityToDto(cartEntity);
    }

    public List<CartDto> getAllCarts(){
        return cartRepository.findAll()
                .stream()
                .map(cartMapper::mapCartEntityToDto)
                .collect(Collectors.toList());
    }

    public Page<CartDto> getPageAllCarts(Pageable pageable){
        return cartRepository.findAll(pageable)
                .map(cartMapper::mapCartEntityToDto);
    }

    public CartDto getCartById(Long id){
        CartEntity cartEntity = cartRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found!"));
        return cartMapper.mapCartEntityToDto(cartEntity);
    }

    public CartDto updateCartItem(Long userId, CartItemDto cartItemDto){

        //Find the cartItem entity
        CartItemEntity existingItemEntity = cartItemRepository.findById(cartItemDto.getId())
                .orElseThrow(()->new RuntimeException("CartItemEntity not found!"));
        CartEntity existingCartEntity = cartRepository.findById(cartItemDto.getCartId())
                .orElseThrow(()->new RuntimeException("CartEntity not found!"));


        //Ensure the cart belongs to the user
        if(!existingCartEntity.getUser().getId().equals(userId)){
            throw new RuntimeException("This cart does not belong to the user!");
        }

        // Check if the requested quantity is available in stock
        ProductEntity productEntity = existingItemEntity.getProduct();
        if(productEntity.getStockQuantity() < cartItemDto.getQuantity()){
            throw new RuntimeException("Not enough stock available for product: " + productEntity.getName());
        }

        //Update quantity
        existingItemEntity.setQuantity(cartItemDto.getQuantity());

        // Recalculate total price for this item
        double totalPrice = existingItemEntity.getQuantity() * productEntity.getPrice() * (1 - productEntity.getDiscountPercentage()/100);
        existingItemEntity.setTotalPrice(totalPrice);

        cartItemRepository.save(existingItemEntity);

        // Recalculate total cart price
        //existingCartEntity.getCartItemList().add(existingItemEntity);
        double totalCartPrice = existingCartEntity.getCartItemList()
                .stream()
                .mapToDouble(CartItemEntity::getTotalPrice)
                .sum();
        existingCartEntity.setTotalCartPrice(totalCartPrice);
        cartRepository.save(existingCartEntity);

        return cartMapper.mapCartEntityToDto(existingCartEntity);
    }

    public void deleteCart(Long id){
        cartRepository.deleteById(id);
    }
}
