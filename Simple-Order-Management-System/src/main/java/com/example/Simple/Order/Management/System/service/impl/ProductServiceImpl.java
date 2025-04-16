package com.example.Simple.Order.Management.System.service.impl;

import com.example.Simple.Order.Management.System.dto.ProductRequestDto;
import com.example.Simple.Order.Management.System.dto.ProductResponseDto;
import com.example.Simple.Order.Management.System.exception.ResourceNotFoundException;
import com.example.Simple.Order.Management.System.model.Product;
import com.example.Simple.Order.Management.System.repository.ProductRepository;
import com.example.Simple.Order.Management.System.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = modelMapper.map(requestDto, Product.class);
        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponseDto.class);
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID" + id + " not found"));
        return modelMapper.map(product, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID" + id + " not found"));

        product.setName(requestDto.getName());
        product.setPrice(requestDto.getPrice());
        product.setStock(requestDto.getStock());

        Product updated = productRepository.save(product);
        return modelMapper.map(updated, ProductResponseDto.class);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID" + id + " not found"));
        productRepository.delete(product);
    }
}
