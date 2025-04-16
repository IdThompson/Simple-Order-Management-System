package com.example.Simple.Order.Management.System.repository;

import com.example.Simple.Order.Management.System.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
