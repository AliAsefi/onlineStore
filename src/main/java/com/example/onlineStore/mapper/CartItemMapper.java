package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.CartItemDto;
import com.example.onlineStore.entity.CartItemEntity;
import com.example.onlineStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    @Autowired
    private ProductRepository productRepository;

    public CartItemEntity mapCartItemDtoToEntity(CartItemDto cartItemDto){
        CartItemEntity cartItemEntity = new CartItemEntity();

        cartItemEntity.setId(cartItemDto.getId());
        cartItemEntity.setQuantity(cartItemDto.getQuantity());
        cartItemEntity.setPricePerUnit(cartItemDto.getPricePerUnit());
        cartItemEntity.setDiscountPercentage(cartItemDto.getDiscountPercentage());
        cartItemEntity.setTotalPrice(cartItemDto.getTotalPrice());

        return cartItemEntity;
    }

    public CartItemDto mapCartItemEntityToDto(CartItemEntity cartItemEntity){
        CartItemDto cartItemDto = new CartItemDto();

        cartItemDto.setId(cartItemEntity.getId());
        cartItemDto.setQuantity(cartItemEntity.getQuantity());
        cartItemDto.setPricePerUnit(cartItemEntity.getPricePerUnit());
        cartItemDto.setDiscountPercentage(cartItemEntity.getDiscountPercentage());
        cartItemDto.setTotalPrice(cartItemEntity.getTotalPrice());

        cartItemDto.setProductId(cartItemEntity.getProduct().getId());
        cartItemDto.setCartId(cartItemEntity.getCart().getId());

        return cartItemDto;
    }
}
