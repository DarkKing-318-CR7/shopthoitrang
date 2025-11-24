package com.uth.shoptmdt.service;

import com.uth.shoptmdt.entity.Order;
import com.uth.shoptmdt.entity.OrderStatus;
import com.uth.shoptmdt.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService{

    private final OrderRepository repo;


    public List<Order> findAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Order> findById(Long id) {
        return repo.findById(id);
    }

    public Order save(Order order) {
        return repo.save(order);
    }

    public long countPendingOrders() {
        return repo.countByStatus(OrderStatus.PENDING);
    }

    public BigDecimal getTotalRevenue() {
        return repo.sumTotalRevenue();
    }

    public List<Order> getLatestPendingOrders() {
        return repo.findTop5ByStatusOrderByCreatedAtDesc(OrderStatus.PENDING);
    }

}
