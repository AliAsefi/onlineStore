package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CartItemDto;
import com.example.onlineStore.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/cartItems")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/page")
    public ResponseEntity<Page<CartItemDto>> getPageAllCartItems(Pageable pageable){
        return new ResponseEntity<>(cartItemService.getPageAllCartItems(pageable),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getAllCartItems(){
        return new ResponseEntity<>(cartItemService.getAllCartItems(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemDto> getCartItemById(@PathVariable("id") Long id){
        return new ResponseEntity<>(cartItemService.getCartItemById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("id") Long id){
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok("CartItem deleted successfully.");
    }
}
