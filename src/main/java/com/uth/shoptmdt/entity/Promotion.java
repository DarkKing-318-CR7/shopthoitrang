package com.uth.shoptmdt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã giảm giá: GIAM10, GIAM20...
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountType discountType;  // PERCENT hoặc FIXED

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal discountValue;   // 10 = 10% hoặc 50000 = 50k

    @Column(precision = 15, scale = 2)
    private BigDecimal minOrderAmount;  // đơn tối thiểu, có thể null

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean active = true;

    public boolean isValidNow(BigDecimal orderTotal) {
        if (!active) return false;
        LocalDateTime now = LocalDateTime.now();

        if (startDate != null && now.isBefore(startDate)) return false;
        if (endDate != null && now.isAfter(endDate)) return false;

        if (minOrderAmount != null &&
                orderTotal != null &&
                orderTotal.compareTo(minOrderAmount) < 0) {
            return false;
        }
        return true;
    }

    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        if (orderTotal == null) return BigDecimal.ZERO;
        if (!isValidNow(orderTotal)) return BigDecimal.ZERO;

        if (discountType == DiscountType.PERCENT) {
            BigDecimal percent = discountValue
                    .divide(BigDecimal.valueOf(100));
            return orderTotal.multiply(percent);
        } else { // FIXED
            BigDecimal discount = discountValue;
            if (discount.compareTo(orderTotal) > 0) {
                discount = orderTotal;
            }
            return discount;
        }
    }

    public enum DiscountType {
        PERCENT, FIXED
    }
}
