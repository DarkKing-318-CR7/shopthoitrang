package com.uth.shoptmdt.repository;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class RevenuePoint {

    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    public RevenuePoint(LocalDateTime orderDate, BigDecimal totalAmount) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
