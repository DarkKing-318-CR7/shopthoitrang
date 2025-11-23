package com.uth.shoptmdt.controller.admin;

import com.uth.shoptmdt.entity.Order;
import com.uth.shoptmdt.entity.OrderStatus;
import com.uth.shoptmdt.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // LIST
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("pageTitle", "Quản lý đơn hàng");
        model.addAttribute("orders", orderService.findAll());
        return "admin/order-list";
    }

    // VIEW DETAIL
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng id=" + id));

        model.addAttribute("pageTitle", "Chi tiết đơn hàng #" + order.getCode());
        model.addAttribute("order", order);
        return "admin/order-detail";
    }

    // UPDATE STATUS (ví dụ: PENDING, PAID, SHIPPED, CANCELED,...)
    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam("status") String status) {

        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        // Convert String → Enum
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);

        orderService.save(order);

        return "redirect:/admin/orders/" + id + "?updated=true";
    }

}
