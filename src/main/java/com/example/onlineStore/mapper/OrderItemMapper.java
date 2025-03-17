package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.OrderItemDto;
import com.example.onlineStore.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemEntity mapOrderItemDtoToEntity(OrderItemDto orderItemDto){
        OrderItemEntity cartItemEntity = new OrderItemEntity();

        cartItemEntity.setId(orderItemDto.getId());
        cartItemEntity.setQuantity(orderItemDto.getQuantity());
        cartItemEntity.setPricePerUnit(orderItemDto.getPricePerUnit());
        cartItemEntity.setDiscountPercentage(orderItemDto.getDiscountPercentage());
        cartItemEntity.setTotalPrice(orderItemDto.getTotalPrice());

        return cartItemEntity;
    }

    public OrderItemDto mapOrderItemEntityToDto(OrderItemEntity orderItemEntity){
        OrderItemDto orderItemDto = new OrderItemDto();

        orderItemDto.setId(orderItemEntity.getId());
        orderItemDto.setQuantity(orderItemEntity.getQuantity());
        orderItemDto.setPricePerUnit(orderItemEntity.getPricePerUnit());
        orderItemDto.setDiscountPercentage(orderItemEntity.getDiscountPercentage());
        orderItemDto.setTotalPrice(orderItemEntity.getTotalPrice());

        orderItemDto.setProductId(orderItemEntity.getProduct().getId());
        orderItemDto.setOrderId(orderItemEntity.getOrder().getId());

        return orderItemDto;
    }
}
