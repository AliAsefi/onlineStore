package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.AddressDto;
import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private CartMapper cartMapper;

    public UserDto mapUserEntityToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();

        userDto.setId(userEntity.getId());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setUsername(userEntity.getUsername());
        userDto.setGender(userEntity.getGender());
        userDto.setBirthDate(userEntity.getBirthDate());
        userDto.setPhone(userEntity.getPhone());
        userDto.setEmail(userEntity.getEmail());

        // Do not map the password field (Entity to Dto)
        //userDto.setPassword(userEntity.getPassword());

        List<AddressDto> addressDtoList = userEntity.getAddressList()
                .stream()
                .map(addressEntity->addressMapper.mapAddressEntityToDto(addressEntity))
                .collect(Collectors.toList());
        userDto.setAddressList(addressDtoList);

        return userDto;
    }


    public UserEntity mapUserDtoToEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(userDto.getId());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setGender(userDto.getGender());
        userEntity.setBirthDate(userDto.getBirthDate());
        userEntity.setPhone(userDto.getPhone());
        userEntity.setEmail(userDto.getEmail());

        userEntity.setPassword(userDto.getPassword());
        // Map the password from DTO to Entity and hash it
        //userEntity.setPassword(userService.hashPassword(userDto.getPassword()));

        userEntity.setAddressList(userDto.getAddressList()
                .stream()
                .map(x->addressMapper.mapAddressDtoToEntity(x,userEntity))
                .collect(Collectors.toList()));

        return userEntity;
    }
}
