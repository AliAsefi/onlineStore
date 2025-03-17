package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    public OrderEntity mapOrderDtoToEntity(OrderDto orderDto){
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setId(orderDto.getId());
        orderEntity.setTotalOrderPrice(orderDto.getTotalOrderPrice());
        orderEntity.setOrderDate(orderDto.getOrderDate());
        orderEntity.setOrderStatus(orderDto.getOrderStatus());

        orderEntity.setOrderItemList(orderDto.getOrderItemList()
                .stream()
                .map(orderItemMapper::mapOrderItemDtoToEntity)
                .collect(Collectors.toList()));

        return orderEntity;
    }

    public OrderDto mapOrderEntityToDto(OrderEntity orderEntity){
        OrderDto orderDto = new OrderDto();

        orderDto.setId(orderEntity.getId());
        orderDto.setTotalOrderPrice(orderEntity.getTotalOrderPrice());
        orderDto.setOrderDate(orderEntity.getOrderDate());
        orderDto.setOrderStatus(orderEntity.getOrderStatus());

        orderDto.setUserId(orderEntity.getUser().getId());
        orderDto.setAddressId(orderEntity.getAddress().getId());
        if(orderEntity.getPayment() != null){
            orderDto.setPaymentId(orderEntity.getPayment().getId());
        }
        orderDto.setOrderItemList(orderEntity.getOrderItemList()
                .stream()
                .map(orderItemMapper::mapOrderItemEntityToDto)
                .collect(Collectors.toList()));

        return orderDto;
    }
}