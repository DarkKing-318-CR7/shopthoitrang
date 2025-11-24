package com.uth.shoptmdt.entity;

import com.uth.shoptmdt.entity.OrderStatus;
import com.uth.shoptmdt.entity.OrderItem;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "orders")
@Getter @Setter @NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 32)
    private String code;

    // Thông tin người nhận
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String addressLine;

    private String province;
    private String district;
    private String ward;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 500)
    private String note;

    private String paymentMethod; // COD, BANKING,…

    @PrePersist
    void prePersist() {
        if (code == null || code.isBlank()) {
            code = "ORD-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) +
                    "-" + UUID.randomUUID().toString().substring(0, 4);
        }
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // tổng tiền trước giảm

    @Column(precision = 15, scale = 2)
    private BigDecimal appliedDiscount; // số tiền đã giảm

    @Column(precision = 15, scale = 2)
    private BigDecimal finalAmount;     // tổng cuối cùng sau giảm

    // Có thể lưu string code hoặc quan hệ:
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public void applyPromotion(Promotion promo) {
        if (promo == null || totalAmount == null) {
            this.appliedDiscount = BigDecimal.ZERO;
            this.finalAmount = totalAmount;
            this.promotion = null;
            return;
        }

        BigDecimal discount = promo.calculateDiscount(totalAmount);
        this.appliedDiscount = discount;
        this.finalAmount = totalAmount.subtract(discount);
        this.promotion = promo;
    }
}
