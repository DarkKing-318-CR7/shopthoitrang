// src/main/java/com/uth/shoptmdt/service/CheckoutService.java
package com.uth.shoptmdt.service;

import com.uth.shoptmdt.entity.*;
import com.uth.shoptmdt.repository.OrderRepository;
import com.uth.shoptmdt.repository.ProductRepository;
import com.uth.shoptmdt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Long placeOrder(String username, CheckoutForm form) {
        if (cartService.isEmpty()) throw new IllegalStateException("Giỏ hàng rỗng");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user: " + username));

        Order order = new Order();
        order.setUser(user);

        // map từ form từ Shopping Cart (fullName/phone/addressLine/…)
        order.setFullName(form.getFullName());
        order.setPhone(form.getPhone());
        order.setAddressLine(form.getAddressLine());
        order.setProvince(form.getProvince());
        order.setDistrict(form.getDistrict());
        order.setWard(form.getWard());
        order.setNote(form.getNote());

        order.setPaymentMethod(form.getPaymentMethod()); // "COD"
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cartService.getItems()) {
            Product p = productRepository.findById(ci.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("SP không tồn tại: " + ci.getProduct().getId()));

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(p);

            // QUAN TRỌNG: dùng đúng field đã map tên cột
            oi.setUnitPrice(p.getPrice());
            oi.setQty(ci.getQty());           // nếu giữ cả qty & quantity
            oi.setQuantity(ci.getQty());

            // có @PrePersist sẽ tự tính, nhưng mình gán rõ ràng luôn:
            oi.setLineAmount(p.getPrice().multiply(BigDecimal.valueOf(ci.getQty())));

            order.getItems().add(oi);

            total = total.add(oi.getLineAmount());
        }


        order.setTotalAmount(total);
        order.setTotal(total); // nếu bảng còn cột 'total'

        Order saved = orderRepository.save(order); // @PrePersist sẽ tự tạo 'code' nếu còn trống
        cartService.clear();
        return saved.getId();
    }
}
