package com.uth.shoptmdt.repository;

import com.uth.shoptmdt.entity.Order;
import com.uth.shoptmdt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByCode(String code);
    Order findByCode(String code);
    Optional<Order> findByIdAndUser_Username(Long id, String username);

    // Danh sách đơn của 1 user
    List<Order> findByUser(User user);

    // Hoặc nếu muốn theo username và sắp xếp mới nhất trước
    List<Order> findByUser_UsernameOrderByCreatedAtDesc(String username);
}
