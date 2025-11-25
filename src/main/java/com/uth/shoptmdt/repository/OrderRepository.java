package com.uth.shoptmdt.repository;

import com.uth.shoptmdt.entity.Order;
import com.uth.shoptmdt.entity.OrderStatus;
import com.uth.shoptmdt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByCode(String code);
    Order findByCode(String code);
    Optional<Order> findByIdAndUser_Username(Long id, String username);

    // Đơn của 1 user
    List<Order> findByUser(User user);

    // Đơn của 1 user (mới nhất trước)
    List<Order> findByUser_UsernameOrderByCreatedAtDesc(String username);

    // Tất cả đơn (mới nhất trước)
    List<Order> findAllByOrderByCreatedAtDesc();

    long countByStatus(OrderStatus status);

    // Tổng doanh thu (PAID + SHIPPED)
    @Query("""
           SELECT COALESCE(SUM(o.finalAmount), 0)
           FROM Order o
           WHERE o.status IN (com.uth.shoptmdt.entity.OrderStatus.PAID,
                              com.uth.shoptmdt.entity.OrderStatus.SHIPPED)
           """)
    BigDecimal sumTotalRevenue();

    // 5 đơn PENDING mới nhất
    List<Order> findTop5ByStatusOrderByCreatedAtDesc(OrderStatus status);

    // Thống kê doanh thu theo thời gian
    @Query("""
    SELECT FUNCTION('DATE', o.createdAt),
           SUM(COALESCE(o.finalAmount, o.totalAmount, 0))
    FROM Order o
    WHERE o.status IN (com.uth.shoptmdt.entity.OrderStatus.PAID,
                       com.uth.shoptmdt.entity.OrderStatus.SHIPPED)
      AND o.createdAt BETWEEN :from AND :to
    GROUP BY FUNCTION('DATE', o.createdAt)
    ORDER BY FUNCTION('DATE', o.createdAt)
""")
    List<Object[]> getRevenueStats(LocalDateTime from, LocalDateTime to);

}
