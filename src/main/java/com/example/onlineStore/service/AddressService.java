package com.example.onlineStore.service;

import com.example.onlineStore.dto.AddressDto;
import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.AddressEntity;
import com.example.onlineStore.entity.ProductEntity;
import com.example.onlineStore.entity.UserEntity;
import com.example.onlineStore.mapper.AddressMapper;
import com.example.onlineStore.repository.AddressRepository;
import com.example.onlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AddressDto createAddress(AddressDto addressDto){

        UserEntity userEntity = userRepository.findById(addressDto.getUserId())
                .orElseThrow(()->new RuntimeException("User not found!"));

        AddressEntity addressEntity = addressMapper.mapAddressDtoToEntity(addressDto, userEntity);
        addressRepository.save(addressEntity);
        return addressMapper.mapAddressEntityToDto(addressEntity);
    }

    public List<AddressDto> getAllAddresses(){
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::mapAddressEntityToDto)
                .collect(Collectors.toList());
    }

    public Page<AddressDto> getPageAllAddresses(Pageable pageable){
        return addressRepository.findAll(pageable)
                .map(addressMapper::mapAddressEntityToDto);
    }

    public AddressDto getAddressById(Long id){
        AddressEntity addressEntity = addressRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Address not found!"));
        return addressMapper.mapAddressEntityToDto(addressEntity);
    }

    @Transactional
    public AddressDto updateAddress(Long id, AddressDto addressDto){

        AddressEntity existingAddressEntity = addressRepository.findById(id)
                        .orElseThrow(()->new RuntimeException("Address not found!"));

        existingAddressEntity.setAddress(addressDto.getAddress());
        existingAddressEntity.setPostalCode(addressDto.getPostalCode());
        existingAddressEntity.setCity(addressDto.getCity());
        existingAddressEntity.setCountry(addressDto.getCountry());
        existingAddressEntity.setState(addressDto.getState());

        AddressEntity updatedAddress = addressRepository.save(existingAddressEntity);
        return addressMapper.mapAddressEntityToDto(updatedAddress);
    }

    public void deleteAddress(Long id){
        addressRepository.deleteById(id);
    }
}
