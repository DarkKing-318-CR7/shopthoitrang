package com.uth.shoptmdt.entity;

import com.uth.shoptmdt.entity.Product;
import com.uth.shoptmdt.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// com.uth.shoptmdt.entity.OrderItem
@Entity
@Table(name = "order_items")
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // DB dùng unit_price -> map đúng tên cột
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    // Một số schema có cả qty và quantity (trùng ý nghĩa).
    // Nếu bảng của bạn CÓ CẢ HAI và đều NOT NULL thì map và set cả hai.
    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // DB bắt buộc NOT NULL
    @Column(name = "line_amount", nullable = false)
    private BigDecimal lineAmount;

    @PrePersist
    @PreUpdate
    void calc() {
        if (quantity == null) quantity = qty;        // đồng bộ
        if (qty == null) qty = quantity;
        if (unitPrice == null) unitPrice = BigDecimal.ZERO;
        if (qty == null) qty = 0;
        lineAmount = unitPrice.multiply(BigDecimal.valueOf(qty));
    }
}
