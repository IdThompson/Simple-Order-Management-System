package com.example.Simple.Order.Management.System.service;

import com.example.Simple.Order.Management.System.dto.OrderRequestDto;
import com.example.Simple.Order.Management.System.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    Page<OrderResponseDto> getOrdersByUser(Long userId, Pageable pageable);
}
