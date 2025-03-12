package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CartDto;
import com.example.onlineStore.dto.CartItemDto;
import com.example.onlineStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<CartDto> createCart(@PathVariable Long userId,
                                               @RequestBody CartItemDto cartItemDto){
        return new ResponseEntity<>(cartService.createCart(userId, cartItemDto), HttpStatus.CREATED);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CartDto>> getPageAllCarts(Pageable pageable){
        return new ResponseEntity<>(cartService.getPageAllCarts(pageable),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCarts(){
        return new ResponseEntity<>(cartService.getAllCarts(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCartById(@PathVariable("id") Long id){
        return new ResponseEntity<>(cartService.getCartById(id), HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<CartDto> updateCartItem(@PathVariable("userId") Long userId,
                                                   @RequestBody CartItemDto cartItemDto){
        return new ResponseEntity<>(cartService.updateCartItem(userId, cartItemDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable("id") Long id){
        cartService.deleteCart(id);
        return ResponseEntity.ok("Cart deleted successfully.");
    }
}
