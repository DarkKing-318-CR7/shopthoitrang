package com.uth.shoptmdt.repository;

import com.uth.shoptmdt.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByCode(String code);
    Order findByCode(String code);
    Optional<Order> findByIdAndUser_Username(Long id, String username);
}
