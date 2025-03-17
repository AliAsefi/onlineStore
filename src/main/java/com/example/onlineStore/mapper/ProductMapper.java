package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity mapProductDtoToEntity(ProductDto productDto){
        ProductEntity productEntity = new ProductEntity();

        productEntity.setId(productDto.getId());
        productEntity.setName(productDto.getName());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setImage(productDto.getImage());
        productEntity.setStockQuantity(productDto.getStockQuantity());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setDiscountPercentage(productDto.getDiscountPercentage());
        productEntity.setProductCategory(productDto.getProductCategory());

        return productEntity;
    }

    public ProductDto mapProductEntityToDto(ProductEntity productEntity){
        ProductDto productDto = new ProductDto();

        productDto.setId(productEntity.getId());
        productDto.setName(productEntity.getName());
        productDto.setDescription(productEntity.getDescription());
        productDto.setImage(productEntity.getImage());
        productDto.setStockQuantity(productEntity.getStockQuantity());
        productDto.setPrice(productEntity.getPrice());
        productDto.setDiscountPercentage(productEntity.getDiscountPercentage());
        productDto.setProductCategory(productEntity.getProductCategory());

        return productDto;
    }
}
