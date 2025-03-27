package com.example.onlineStore.controller;

import com.example.onlineStore.dto.OrderItemDto;
import com.example.onlineStore.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/orderItems")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/page")
    public ResponseEntity<Page<OrderItemDto>> getPageAllOrderItems(Pageable pageable){
        return new ResponseEntity<>(orderItemService.getPageAllOrderItems(pageable),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderItemDto>> getAllOrderItems(){
        return new ResponseEntity<>(orderItemService.getAllOrderItems(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable("id") Long id){
        return new ResponseEntity<>(orderItemService.getOrderItemById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable("id") Long id){
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok("OrderItem deleted successfully.");
    }
}
