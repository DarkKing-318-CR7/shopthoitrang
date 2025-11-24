// src/main/java/com/uth/shoptmdt/service/CheckoutService.java
package com.uth.shoptmdt.service;

import com.uth.shoptmdt.entity.*;
import com.uth.shoptmdt.repository.*;
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
    private final PromotionRepository promotionRepository;   // <<< THÊM MỚI

    @Transactional
    public Long placeOrder(String username, CheckoutForm form) {
        if (cartService.isEmpty()) throw new IllegalStateException("Giỏ hàng rỗng");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user: " + username));

        Order order = new Order();
        order.setUser(user);

        // map từ form
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

            oi.setUnitPrice(p.getPrice());
            oi.setQty(ci.getQty());
            oi.setQuantity(ci.getQty());

            oi.setTotal_amount(p.getPrice().multiply(BigDecimal.valueOf(ci.getQty())));

            order.getItems().add(oi);

            total = total.add(oi.getTotal_amount());
        }

        // Tổng trước giảm
        order.setTotalAmount(total);

        // ===== ÁP DỤNG MÃ GIẢM GIÁ =====
        String code = form.getPromoCode();
        if (code != null && !code.isBlank()) {
            Promotion promo = promotionRepository
                    .findByCodeIgnoreCase(code.trim())
                    .orElse(null);

            if (promo != null && promo.isValidNow(total)) {
                BigDecimal discount = promo.calculateDiscount(total);
                order.setAppliedDiscount(discount);
                order.setFinalAmount(total.subtract(discount));
                order.setPromotion(promo);
            } else {
                order.setAppliedDiscount(BigDecimal.ZERO);
                order.setFinalAmount(total);
            }
        } else {
            order.setAppliedDiscount(BigDecimal.ZERO);
            order.setFinalAmount(total);
        }
        // ===== HẾT PHẦN KHUYẾN MÃI =====

        Order saved = orderRepository.save(order);
        cartService.clear();
        return saved.getId();
    }
}
