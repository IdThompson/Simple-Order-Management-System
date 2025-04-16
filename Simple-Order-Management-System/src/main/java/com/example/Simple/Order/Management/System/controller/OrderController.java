package com.example.Simple.Order.Management.System.controller;

import com.example.Simple.Order.Management.System.dto.ApiResponse;
import com.example.Simple.Order.Management.System.dto.OrderRequestDto;
import com.example.Simple.Order.Management.System.dto.OrderResponseDto;
import com.example.Simple.Order.Management.System.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(@RequestBody @Valid OrderRequestDto dto) {
        OrderResponseDto created = orderService.createOrder(dto);
        return new ResponseEntity<>(new ApiResponse<>("Order created", created), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponseDto> orders = orderService.getOrdersByUser(userId, pageable);

        ApiResponse<Page<OrderResponseDto>> response = new ApiResponse<>("Orders for user", orders);
        return ResponseEntity.ok(response);
    }

}
