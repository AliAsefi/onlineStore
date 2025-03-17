package com.example.onlineStore.service;

import com.example.onlineStore.dto.OrderItemDto;
import com.example.onlineStore.entity.OrderItemEntity;
import com.example.onlineStore.mapper.OrderItemMapper;
import com.example.onlineStore.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderItemMapper orderItemMapper;

    public List<OrderItemDto> getAllOrderItems(){
        return orderItemRepository.findAll()
                .stream()
                .map(orderItemMapper::mapOrderItemEntityToDto)
                .collect(Collectors.toList());
    }

    public Page<OrderItemDto> getPageAllOrderItems(Pageable pageable){
        return orderItemRepository.findAll(pageable)
                .map(orderItemMapper::mapOrderItemEntityToDto);
    }

    public OrderItemDto getOrderItemById(Long id){
        OrderItemEntity orderItemEntity = orderItemRepository.findById(id)
                .orElseThrow(()->new RuntimeException("OrderItem not found!"));
        return orderItemMapper.mapOrderItemEntityToDto(orderItemEntity);
    }

    public void deleteOrderItem(Long id){
        orderItemRepository.deleteById(id);
    }
}