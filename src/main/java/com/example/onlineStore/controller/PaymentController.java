package com.example.onlineStore.controller;

import com.example.onlineStore.dto.PaymentDto;
import com.example.onlineStore.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentDto> createPayment(@PathVariable Long orderId,
                                                    @RequestBody PaymentDto paymentDto){
        return new ResponseEntity<>(paymentService.createPayment(orderId, paymentDto), HttpStatus.CREATED);
    }
}
