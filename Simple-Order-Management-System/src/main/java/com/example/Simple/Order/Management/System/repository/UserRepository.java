package com.example.Simple.Order.Management.System.repository;

import com.example.Simple.Order.Management.System.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
