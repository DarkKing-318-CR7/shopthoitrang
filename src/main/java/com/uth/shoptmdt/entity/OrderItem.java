package com.uth.shoptmdt.entity;

import com.uth.shoptmdt.entity.Product;
import com.uth.shoptmdt.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity @Table(name = "order_items")
@Getter @Setter @NoArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false) private Integer qty;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal unitPrice;
    @Column(nullable = false, precision = 18, scale = 2) private BigDecimal lineAmount;

    private BigDecimal price;   // snapshot giá tại thời điểm đặt
    private int quantity;
}
