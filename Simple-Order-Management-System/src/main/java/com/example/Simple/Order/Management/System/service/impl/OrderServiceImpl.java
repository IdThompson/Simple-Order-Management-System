package com.example.Simple.Order.Management.System.service.impl;

import com.example.Simple.Order.Management.System.dto.OrderItemRequestDto;
import com.example.Simple.Order.Management.System.dto.OrderItemResponseDto;
import com.example.Simple.Order.Management.System.dto.OrderRequestDto;
import com.example.Simple.Order.Management.System.dto.OrderResponseDto;
import com.example.Simple.Order.Management.System.exception.BadRequestException;
import com.example.Simple.Order.Management.System.exception.ResourceNotFoundException;
import com.example.Simple.Order.Management.System.model.Order;
import com.example.Simple.Order.Management.System.model.OrderItem;
import com.example.Simple.Order.Management.System.model.Product;
import com.example.Simple.Order.Management.System.model.User;
import com.example.Simple.Order.Management.System.repository.OrderRepository;
import com.example.Simple.Order.Management.System.repository.ProductRepository;
import com.example.Simple.Order.Management.System.repository.UserRepository;
import com.example.Simple.Order.Management.System.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getUserId()));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDto.getProductId()));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
            orderItems.add(item);
        }

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalAmount(total)
                .items(new ArrayList<>())
                .build();

        for (OrderItem item : orderItems) {
            item.setOrder(order);
            order.getItems().add(item);
        }

        Order saved = orderRepository.save(order);
        return getOrderResponseDto(saved);
    }

    private OrderResponseDto getOrderResponseDto(Order saved) {
        List<OrderItemResponseDto> itemResponses = saved.getItems().stream().map(item -> OrderItemResponseDto.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .build()).collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(saved.getId())
                .createdAt(saved.getOrderDate())
                .totalAmount(saved.getTotalAmount())
                .userId(saved.getUser().getId())
                .items(itemResponses)
                .build();
    }

    @Override
    public Page<OrderResponseDto> getOrdersByUser(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return orders.map(this::getOrderResponseDto);
    }
}
