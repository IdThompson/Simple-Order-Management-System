package com.example.Simple.Order.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long orderId;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private Long userId;
    private List<OrderItemResponseDto> items;
}
