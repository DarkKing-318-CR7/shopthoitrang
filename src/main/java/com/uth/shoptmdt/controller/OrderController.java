// src/main/java/com/uth/shoptmdt/controller/OrderController.java
package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.entity.Order;
import com.uth.shoptmdt.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/order-success")
    public String success(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id != null) {
            orderRepository.findById(id).ifPresent(o -> model.addAttribute("order", o));
        }
        return "order-success";
    }

    @GetMapping("/orders/{id}")
    public String detail(@PathVariable Long id, Model model, Authentication auth) {

        // Nếu muốn ràng buộc đơn thuộc về user hiện tại:
        // String username = auth != null ? auth.getName() : null;
        // Order order = orderRepository.findByIdAndUser_Username(id, username)
        //        .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        model.addAttribute("order", order);
        return "order-detail";
    }
}
