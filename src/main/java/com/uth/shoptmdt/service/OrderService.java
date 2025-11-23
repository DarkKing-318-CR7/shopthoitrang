package com.uth.shoptmdt.service;

import com.uth.shoptmdt.entity.Order;
import com.uth.shoptmdt.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService{

    private final OrderRepository repo;


    public List<Order> findAll() {
        return repo.findAll();
    }

    public Optional<Order> findById(Long id) {
        return repo.findById(id);
    }

    public Order save(Order order) {
        return repo.save(order);
    }
}
