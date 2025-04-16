package com.example.Simple.Order.Management.System.repository;

import com.example.Simple.Order.Management.System.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
