package com.example.onlineStore.controller;

import com.example.onlineStore.dto.OrderDto;
import com.example.onlineStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long userId,
                                                @RequestBody OrderDto orderDto){
        return new ResponseEntity<>(orderService.createOrder(userId, orderDto), HttpStatus.CREATED);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<OrderDto>> getPageAllOrders(Pageable pageable){
        return new ResponseEntity<>(orderService.getPageAllOrders(pageable),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        return new ResponseEntity<>(orderService.getAllOrders(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id){
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

//    @PutMapping("/update/{userId}")
//    public ResponseEntity<OrderDto> updateOrderItem(@PathVariable("userId") Long userId,
//                                                   @RequestBody OrderItemDto orderItemDto){
//        return new ResponseEntity<>(orderService.updateOrderItem(userId, orderItemDto), HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully.");
    }
}
