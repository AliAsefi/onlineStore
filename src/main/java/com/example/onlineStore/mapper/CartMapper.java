package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.entity.CartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

    @Autowired
    private CartItemMapper cartItemMapper;

    public CartEntity mapCartDtoToEntity(CartDto cartDto){
        CartEntity cartEntity = new CartEntity();

        cartEntity.setId(cartDto.getId());
        cartEntity.setTotalCartPrice(cartDto.getTotalCartPrice());
        cartEntity.setCartItemList(cartDto.getCartItemList()
                .stream()
                .map(cartItemMapper::mapCartItemDtoToEntity)
                .collect(Collectors.toList()));

        return cartEntity;
    }

    public CartDto mapCartEntityToDto(CartEntity cartEntity){
        CartDto cartDto = new CartDto();

        cartDto.setId(cartEntity.getId());
        cartDto.setTotalCartPrice(cartEntity.getTotalCartPrice());

        cartDto.setUserId(cartEntity.getUser().getId());

        cartDto.setCartItemList(cartEntity.getCartItemList()
                .stream()
                .map(cartItemMapper::mapCartItemEntityToDto)
                .collect(Collectors.toList()));

        return cartDto;
    }
}

/*
    OrderMapper should not depend on OrderService
    The OrderMapper class should only map between OrderEntity and OrderDto. It should not contain business logic like calculating the total order price.
    Right now, you autowire OrderService inside OrderMapper, which means OrderMapper depends on OrderService. This is not ideal because the mapper class should remain a simple converter.
 */