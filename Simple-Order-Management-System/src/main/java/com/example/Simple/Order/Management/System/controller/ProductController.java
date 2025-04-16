package com.example.Simple.Order.Management.System.controller;

import com.example.Simple.Order.Management.System.dto.ApiResponse;
import com.example.Simple.Order.Management.System.dto.ProductRequestDto;
import com.example.Simple.Order.Management.System.dto.ProductResponseDto;
import com.example.Simple.Order.Management.System.service.OrderService;
import com.example.Simple.Order.Management.System.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = productService.createProduct(requestDto);
        return new ResponseEntity<>(new ApiResponse<>("Product created", responseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);  // create Pageable from page and size parameters
        Page<ProductResponseDto> productPage = productService.getAllProducts(pageable);  // fetch paginated results

        ApiResponse<Page<ProductResponseDto>> response = new ApiResponse<>("Products retrieved", productPage);
        return ResponseEntity.ok(response);  // return paginated products inside ApiResponse
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("Product fetched", productService.getProductById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = productService.updateProduct(id, requestDto);
        return ResponseEntity.ok(new ApiResponse<>("Product updated", responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>("Product deleted", null));
    }
}
