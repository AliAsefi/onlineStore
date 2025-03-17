package com.example.onlineStore.service;

import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.entity.*;
import com.example.onlineStore.enums.OrderStatus;
import com.example.onlineStore.enums.PaymentStatus;
import com.example.onlineStore.mapper.PaymentMapper;
import com.example.onlineStore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public PaymentDto createPayment(Long orderId, PaymentDto paymentDto){

        // 1. Find the order
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(()->new RuntimeException("Order not found with ID: " + orderId));

        // 2. check the order is paid
        if(orderEntity.getOrderStatus() == OrderStatus.PAID){
            throw new RuntimeException("Order is already paid.");
        }

        // 3. Validate payment amount
        double totalOrderPrice = orderEntity.getTotalOrderPrice();
        if(!paymentDto.getAmountPaid().equals(totalOrderPrice)){
            throw new RuntimeException("Incorrect payment amount. Expected: " + totalOrderPrice);
        }

        // 4. create new payment and set them
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentDate(LocalDate.now());
        paymentEntity.setPaymentMethod(paymentDto.getPaymentMethod());
        paymentEntity.setOrder(orderEntity);
        paymentEntity.setAmountPaid(paymentDto.getAmountPaid());
        paymentEntity.setPaymentStatus(PaymentStatus.COMPLETED);

        // 5. set orderEntity
        orderEntity.setPayment(paymentEntity);
        orderEntity.setOrderStatus(OrderStatus.PAID);
        paymentEntity = paymentRepository.save(paymentEntity); // ✅ SAVE IT FIRST


        // 6. reduce product stockQuantity after successful payment
        for(OrderItemEntity item : orderEntity.getOrderItemList()){
            ProductEntity productEntity = item.getProduct();
            productEntity.setStockQuantity(productEntity.getStockQuantity() - item.getQuantity());
            productRepository.save(productEntity);
        }

        // delete cartItems that has been paid
        CartEntity cartEntity = cartRepository.findByUser_Id(orderEntity.getUser().getId());
        if (cartEntity != null) {

            // Collect cart item IDs that need to be deleted
            List<Long> cartItemIdsToDelete = orderEntity.getOrderItemList().stream()
                    .map(orderItem -> orderItem.getProduct().getId()) // Get product IDs from OrderItems
                    .collect(Collectors.toList());

            // Delete the cart items from the database
            cartItemRepository.deleteByProductIdIn(cartItemIdsToDelete);
        }else{
            paymentEntity.setPaymentStatus(PaymentStatus.FAILED);
            orderEntity.setOrderStatus(OrderStatus.PENDING); // Order remains pending if payment fails
        }

        // 7. save
        orderEntity.setPayment(paymentEntity);
        orderRepository.save(orderEntity);
        paymentRepository.save(paymentEntity);

        return paymentMapper.mapPaymentEntityToDto(paymentEntity);

        /*
           {
            "addressId": 152,
            "amountPaid" : 20.0,
            "paymentMethod" : "CREDIT_CARD"
           }
         */
    }
}
