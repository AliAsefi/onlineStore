package com.example.onlineStore.service;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.dto.OrderItemDto;
import com.example.onlineStore.entity.*;
import com.example.onlineStore.enums.OrderStatus;
import com.example.onlineStore.mapper.OrderMapper;
import com.example.onlineStore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public OrderDto createOrder(Long userId, OrderDto orderDto){

        // Retrieve the cart from userId and the cartItemList from cart
        CartEntity cartEntity = cartRepository.findByUser_Id(userId);
        if(cartEntity == null) throw new RuntimeException("Cart not found for user ID: " + userId);
        List<CartItemEntity> cartItemEntityList = cartEntity.getCartItemList();

        // prepare new orderEntity
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderDate(LocalDate.now());
        orderEntity.setUser(cartEntity.getUser());
        orderEntity.setTotalOrderPrice(0.0);
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        AddressEntity addressEntity = addressRepository.findById(orderDto.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + orderDto.getAddressId()));
        orderEntity.setAddress(addressEntity);

        // prepare new orderItemEntityList
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        List<CartItemEntity> paidCartItems = new ArrayList<>();

        //Convert CartItemEntity → OrderItemEntity
        for(OrderItemDto orderItemDto: orderDto.getOrderItemList()){

            // Find the cart item that matches the selected order item
            CartItemEntity cartItemEntity = cartItemEntityList.stream()
                    .filter(item -> item.getId().equals(orderItemDto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cart item not found: " + orderItemDto.getId()));

            // Retrieve the product
            ProductEntity productEntity = cartItemEntity.getProduct();

            // check the quantity of product
            if (productEntity.getStockQuantity() < cartItemEntity.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + productEntity.getName());
            }

            // Convert to OrderItemEntity
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setOrder(orderEntity);
            orderItemEntity.setProduct(productEntity);
            orderItemEntity.setPricePerUnit(productEntity.getPrice());
            orderItemEntity.setDiscountPercentage(productEntity.getDiscountPercentage());
            orderItemEntity.setQuantity(cartItemEntity.getQuantity());

            double totalPrice = productEntity.getPrice() * cartItemEntity.getQuantity() * (1- productEntity.getDiscountPercentage()/100);
            orderItemEntity.setTotalPrice(totalPrice);

            orderItemEntityList.add(orderItemEntity);

            // Store cart items that will be deleted later if payment succeeds
            paidCartItems.add(cartItemEntity);

            /*
            {
                "addressId": 203,
                "orderItemList": [
                    {
                     "id": 1
                    }
                ]
            }
             */
        }

        double totalOrderPrice = orderItemEntityList.stream()
                .mapToDouble(OrderItemEntity::getTotalPrice)
                .sum();
        orderEntity.setTotalOrderPrice(totalOrderPrice);

        orderEntity.setOrderItemList(orderItemEntityList);
        orderRepository.save(orderEntity);

        return orderMapper.mapOrderEntityToDto(orderEntity);
    }

    public List<OrderDto> getAllOrders(){
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::mapOrderEntityToDto)
                .collect(Collectors.toList());
    }

    public Page<OrderDto> getPageAllOrders(Pageable pageable){
        return orderRepository.findAll(pageable)
                .map(orderMapper::mapOrderEntityToDto);
    }

    public OrderDto getOrderById(Long id){
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Order not found!"));
        return orderMapper.mapOrderEntityToDto(orderEntity);
    }
/*
    public OrderDto updateOrderItem(Long userId, OrderItemDto orderItemDto){

        //Find the cartItem entity
        CartItemEntity existingItemEntity = orderItemRepository.findById(cartItemDto.getId())
                .orElseThrow(()->new RuntimeException("CartItemEntity not found!"));
        CartEntity existingCartEntity = orderRepository.findById(cartItemDto.getCartId())
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

        orderItemRepository.save(existingItemEntity);

        // Recalculate total cart price
        //existingCartEntity.getCartItemList().add(existingItemEntity);
        double totalCartPrice = existingCartEntity.getCartItemList()
                .stream()
                .mapToDouble(CartItemEntity::getTotalPrice)
                .sum();
        existingCartEntity.setTotalCartPrice(totalCartPrice);
        orderRepository.save(existingCartEntity);

        return orderMapper.mapCartEntityToDto(existingCartEntity);
    }
*/
    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }
}
