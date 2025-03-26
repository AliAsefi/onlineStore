//package com.example.onlineStore.controllerTest;
//
//
//import com.example.onlineStore.controller.CartController;
//import com.example.onlineStore.dto.CartDto;
//import com.example.onlineStore.dto.CartItemDto;
//import com.example.onlineStore.dto.ProductDto;
//import com.example.onlineStore.service.CartItemService;
//import com.example.onlineStore.service.CartService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@WebMvcTest(controllers = CartController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class CartControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @MockitoBean
//    private CartService cartService;
//    @MockitoBean
//    private CartItemService cartItemService;
//
//
//    private CartDto cartDto1, cartDto2;
//    private CartItemDto cartItemDto1, cartItemDto2, cartItemDto3;
//    private ProductDto productDto1, productDto2;
//
//
//    @BeforeEach
//    public void init() {
//        cartItemDto1 = CartItemDto.builder()
//                .id(1L)
//                .cartId(1L)
//                .productId(1L)
//                .pricePerUnit(20)
//                .discountPercentage(10)
//                .quantity(1)
//                .totalPrice(20.0 * 1 * (1 - 0.1))
//                .build();
//        cartItemDto2 = CartItemDto.builder().id(2L).cartId(1L).productId(2L).pricePerUnit(20).discountPercentage(10).quantity(2)
//                .totalPrice(20.0 * 2 * (1 - 0.1))
//                .build();
//        cartItemDto3 = CartItemDto.builder().id(3L).cartId(2L).productId(3L).pricePerUnit(20).discountPercentage(10).quantity(3)
//                .totalPrice(20.0 * 3 * (1 - 0.1))
//                .build();
//        cartDto1 = CartDto.builder().id(1L).userId(1L)
//                .totalCartPrice(cartItemDto1.getTotalPrice() + cartItemDto2.getTotalPrice())
//                .build();
//        cartDto2 = CartDto.builder().id(2L).userId(2L)
//                .totalCartPrice(cartItemDto3.getTotalPrice())
//                .build();
//    }
//
//    @Test
//    public void CartController_GetAllCartItems_ReturnCartItemsDto() throws Exception{
//        List<CartItemDto> cartItemDtos1 = List.of(cartItemDto1, cartItemDto2);
//        List<CartItemDto> cartItemDtos2 = List.of(cartItemDto3);
//        cartDto1.setCartItemList(cartItemDtos1);
//        cartDto2.setCartItemList(cartItemDtos2);
//        List<CartDto> cartDtos = List.of(cartDto1, cartDto2);
//
//        when(cartService.getAllCarts()).thenReturn(cartDtos);
//
//        mockMvc.perform(get("/api/carts"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].totalCartPrice").value(cartDto1.getTotalCartPrice()))
//                .andExpect(jsonPath("$[1].totalCartPrice").value(cartDto2.getTotalCartPrice()));
//    }
//}
