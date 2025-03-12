package com.example.onlineStore.controllerTest;


import com.example.onlineStore.controller.UserController;
import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;

    private UserDto userDto, userDto2;

    @BeforeEach
    public void init() {
        userDto = UserDto.builder().id(1L).firstName("a").lastName("b").build();
        userDto2 = UserDto.builder().id(2L).firstName("a").lastName("b").build();


    }

    @Test
    public void UserController_GetAllUsers_ReturnUserDto() throws Exception{
        List<UserDto> userDtos = List.of(userDto,userDto2);
        when(userService.getAllUsers()).thenReturn(userDtos);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("a"))
                .andExpect(jsonPath("$[1].firstName").value("a"));
    }

    @Test
    public void UserController_GetUserById_ReturnUserDto() throws Exception{
        Long id = 1L;
        when(userService.getUserById(id)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("a"))
                .andExpect(jsonPath("$.lastName").value("b"));
    }

    @Test
    public void UserController_CreateUser_ReturnUserDto() throws Exception{

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                //.content("{\"firstName\": \"b\", \"lastName\": \"b\"}"))
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("a"))
                .andExpect(jsonPath("$.lastName").value("b"));
    }
}
