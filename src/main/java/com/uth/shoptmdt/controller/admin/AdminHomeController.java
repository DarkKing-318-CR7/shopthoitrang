// src/main/java/com/uth/shoptmdt/controller/admin/AdminHomeController.java
package com.uth.shoptmdt.controller.admin;

import com.uth.shoptmdt.entity.Order;
import com.uth.shoptmdt.entity.OrderStatus;
import com.uth.shoptmdt.repository.OrderRepository;
import com.uth.shoptmdt.repository.ProductRepository;
import com.uth.shoptmdt.repository.RevenuePoint;
import com.uth.shoptmdt.repository.UserRepository;
import com.uth.shoptmdt.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminHomeController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    @GetMapping
    public String dashboard(
            @RequestParam(name = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(name = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        // ====== DEFAULT RANGE: LAST 7 DAYS ======
        if (to == null) to = LocalDate.now();
        if (from == null) from = to.minusDays(6);

        // convert to LocalDateTime because DB uses LocalDateTime
        LocalDateTime fromDateTime = from.atStartOfDay();           // 00:00 ngày from
        LocalDateTime toDateTime   = to.plusDays(1).atStartOfDay(); // 00:00 ngày tiếp theo

// ===== DASHBOARD STATS =====
        long totalProducts = productRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        long totalUsers    = userRepository.count();



// ===== REVENUE CHART DATA =====
        List<RevenuePoint> revenuePoints =
                orderRepository.getRevenueStats(fromDateTime, toDateTime);

        List<String> labels = revenuePoints.stream()
                .map(p -> p.getOrderDate().toLocalDate().toString())
                .collect(Collectors.toList());

        List<BigDecimal> data = revenuePoints.stream()
                .map(RevenuePoint::getTotalAmount)
                .collect(Collectors.toList());

//        List<Order> recentPendingOrders =
//                orderRepository.findTop5ByStatusOrderByCreatedAtDesc(OrderStatus.PENDING);

// ===== SET MODEL =====
        model.addAttribute("pageTitle", "Trang quản trị");
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("totalUsers", totalUsers);

        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("revenueLabels", labels);
        model.addAttribute("revenueData", data);
        model.addAttribute("latestPendingOrders", orderService.getLatestPendingOrders());
        return "admin/dashboard";
    }
}
