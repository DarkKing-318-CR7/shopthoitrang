package com.uth.shoptmdt.entity;

import com.uth.shoptmdt.entity.OrderStatus;
import com.uth.shoptmdt.entity.OrderItem;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "orders")
@Getter @Setter @NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã đơn (để hiển thị đẹp)
    @Column(unique = true, nullable = false, length = 20)
    private String code;

    // Thông tin nhận hàng
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String addressLine;
    private String province;
    private String district;
    private String ward;

    // Tổng tiền
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // Trạng thái
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    // Thời gian
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    // Items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id")
    private User user;

    // thông tin nhận hàng
    @Column(length = 100)
    private String receiverName;

    @Column(length = 20)
    private String receiverPhone;

    @Column(length = 255)
    private String receiverAddress;

    @Column(length = 500)
    private String note;

    // thanh toán / trạng thái
    @Column(length = 20)
    private String paymentMethod;      // ví dụ: "COD"


    private BigDecimal total;          // tổng tiền
}
