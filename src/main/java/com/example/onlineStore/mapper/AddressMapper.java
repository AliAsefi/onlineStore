package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.AddressDto;
import com.example.onlineStore.entity.AddressEntity;
import com.example.onlineStore.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto mapAddressEntityToDto(AddressEntity addressEntity){
        AddressDto addressDto = new AddressDto();

        addressDto.setId(addressEntity.getId());
        addressDto.setUserId(addressEntity.getUser().getId());
        addressDto.setAddress(addressEntity.getAddress());
        addressDto.setCity(addressEntity.getCity());
        addressDto.setState(addressEntity.getState());
        addressDto.setCountry(addressEntity.getCountry());
        addressDto.setPostalCode(addressEntity.getPostalCode());

        return addressDto;
    }

    public AddressEntity mapAddressDtoToEntity(AddressDto addressDto, UserEntity userEntity){
        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setId(addressDto.getId());
        addressEntity.setUser(userEntity);
        addressEntity.setAddress(addressDto.getAddress());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setState(addressDto.getState());
        addressEntity.setCountry(addressDto.getCountry());
        addressEntity.setPostalCode(addressDto.getPostalCode());

        return addressEntity;
    }
}
