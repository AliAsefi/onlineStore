package com.example.onlineStore.controller;

import com.example.onlineStore.dto.AddressDto;
import com.example.onlineStore.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/create")
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto){
        return new ResponseEntity<>(addressService.createAddress(addressDto), HttpStatus.CREATED);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<AddressDto>> getAllAddresses(Pageable pageable){
        return new ResponseEntity<>(addressService.getPageAllAddresses(pageable),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses(){
        return new ResponseEntity<>(addressService.getAllAddresses(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable("id") Long id){
        return new ResponseEntity<>(addressService.getAddressById(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable("id") Long id,
                                                    @RequestBody AddressDto addressDto){
        return new ResponseEntity<>(addressService.updateAddress(id,addressDto), HttpStatus.OK);
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") Long id){
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully.");
    }
}
