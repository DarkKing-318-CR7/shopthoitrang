// src/main/java/com/uth/shoptmdt/controller/admin/AdminHomeController.java

package com.uth.shoptmdt.controller.admin;

import com.uth.shoptmdt.entity.OrderStatus;
import com.uth.shoptmdt.repository.OrderRepository;
import com.uth.shoptmdt.repository.ProductRepository;
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
import java.util.ArrayList;
import java.util.List;

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

        if (to == null) to = LocalDate.now();
        if (from == null) from = to.minusDays(6);

        LocalDateTime fromDT = from.atStartOfDay();
        LocalDateTime toDT   = to.plusDays(1).atStartOfDay();

        long totalProducts = productRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        long totalUsers    = userRepository.count();

        // Lấy dữ liệu doanh thu dạng Object[]
        List<Object[]> rows = orderRepository.getRevenueStats(fromDT, toDT);

        List<String> labels = new ArrayList<>();
        List<BigDecimal> dailyData = new ArrayList<>();
        List<BigDecimal> cumulativeData = new ArrayList<>();

        BigDecimal cumulative = BigDecimal.ZERO;

        for (Object[] r : rows) {

            // ----- DATE -----
            Object d = r[0];
            LocalDate date;

            if (d instanceof LocalDate ld) date = ld;
            else if (d instanceof java.sql.Date sd) date = sd.toLocalDate();
            else if (d instanceof LocalDateTime ldt) date = ldt.toLocalDate();
            else date = null;

            labels.add(date != null ? date.toString() : d.toString());

            // ----- DAILY AMOUNT -----
            BigDecimal dayAmount = (BigDecimal) r[1];
            if (dayAmount == null) dayAmount = BigDecimal.ZERO;

            dailyData.add(dayAmount);

            // ----- CUMULATIVE -----
            cumulative = cumulative.add(dayAmount);
            cumulativeData.add(cumulative);
        }

        // Gửi về View
        model.addAttribute("pageTitle", "Trang quản trị");
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("totalUsers", totalUsers);

        model.addAttribute("from", from);
        model.addAttribute("to", to);

        model.addAttribute("revenueLabels", labels);
        model.addAttribute("revenueDailyData", dailyData);       // biểu đồ cột
        model.addAttribute("revenueCumulativeData", cumulativeData); // biểu đồ line

        model.addAttribute("latestPendingOrders", orderService.getLatestPendingOrders());

        return "admin/dashboard";
    }
}
