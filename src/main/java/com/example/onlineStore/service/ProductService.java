package com.example.onlineStore.service;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.ProductEntity;
import com.example.onlineStore.mapper.ProductMapper;
import com.example.onlineStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductDto createProduct(ProductDto productDto){
        ProductEntity productEntity = productMapper.mapProductDtoToEntity(productDto);
        productRepository.save(productEntity);
        return productMapper.mapProductEntityToDto(productEntity);
    }

    public List<ProductDto> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapProductEntityToDto)
                .collect(Collectors.toList());
    }

    public Page<ProductDto> getPageAllProducts(Pageable pageable){
        return productRepository.findAll(pageable)
                .map(productMapper::mapProductEntityToDto);
    }

    public ProductDto getProductById(Long id){
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found!"));
        return productMapper.mapProductEntityToDto(productEntity);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto){
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found!"));

        productEntity.setName(productDto.getName());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setImage(productDto.getImage());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setDiscountPercentage(productDto.getDiscountPercentage());
        productEntity.setStockQuantity(productDto.getStockQuantity());
        productEntity.setProductCategory(productDto.getProductCategory());

        productRepository.save(productEntity);
        return productMapper.mapProductEntityToDto(productEntity);
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
