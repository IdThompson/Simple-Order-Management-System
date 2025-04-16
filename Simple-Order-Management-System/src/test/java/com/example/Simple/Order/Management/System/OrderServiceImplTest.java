package com.example.Simple.Order.Management.System;

import com.example.Simple.Order.Management.System.dto.OrderItemRequestDto;
import com.example.Simple.Order.Management.System.dto.OrderRequestDto;
import com.example.Simple.Order.Management.System.dto.OrderResponseDto;
import com.example.Simple.Order.Management.System.exception.BadRequestException;
import com.example.Simple.Order.Management.System.exception.ResourceNotFoundException;
import com.example.Simple.Order.Management.System.model.Order;
import com.example.Simple.Order.Management.System.model.Product;
import com.example.Simple.Order.Management.System.model.User;
import com.example.Simple.Order.Management.System.repository.OrderRepository;
import com.example.Simple.Order.Management.System.repository.ProductRepository;
import com.example.Simple.Order.Management.System.repository.UserRepository;
import com.example.Simple.Order.Management.System.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Product product;
    private OrderRequestDto orderRequestDto;

    @BeforeEach
    public void setUp() {
        // Set up the mock data
        user = new User("Idowu thompson", "idowuthompson@gmail.com");
        product = new Product("Laptop", new BigDecimal("1200.00"), 100);

        orderRequestDto = new OrderRequestDto(user.getId(),
                Collections.singletonList(new OrderItemRequestDto(product.getId(), 2)));
    }

    @Test
    public void testCreateOrderSuccess() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setUser(user);
        savedOrder.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(2)));
        when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(savedOrder);
        OrderResponseDto response = orderService.createOrder(orderRequestDto);
        assertEquals(1L, response.getOrderId());
        assertEquals(user.getId(), response.getUserId());
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(2)), response.getTotalAmount());
    }

    @Test
    public void testCreateOrderWithInvalidUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(orderRequestDto);
        });
        assertEquals("User not found with ID: " + user.getId(), exception.getMessage());
    }

    @Test
    public void testCreateOrderWithInsufficientStock() {
        product.setStock(1);
        orderRequestDto.setItems(Collections.singletonList(new OrderItemRequestDto(product.getId(), 2)));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            orderService.createOrder(orderRequestDto);
        });
        assertEquals("Insufficient stock for product: " + product.getName(), exception.getMessage());
    }

    @Test
    public void testGetOrdersByUser() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        Page<Order> ordersPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findByUserId(eq(user.getId()), ArgumentMatchers.any(Pageable.class))).thenReturn(ordersPage);
        Page<OrderResponseDto> orders = orderService.getOrdersByUser(user.getId(), Pageable.unpaged());
        assertEquals(1, orders.getContent().size());
        assertEquals(user.getId(), orders.getContent().get(0).getUserId());
    }
}
