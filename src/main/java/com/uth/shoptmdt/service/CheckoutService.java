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

    private final CartService cartService;          // dùng giỏ trong session hiện tại
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    /**
     * Tạo đơn hàng từ giỏ hiện tại của Session và thông tin form
     * @param username người mua (đang đăng nhập)
     * @param form     thông tin nhận hàng/thanh toán
     * @return orderId của đơn đã tạo
     */
    @Transactional
    public Long placeOrder(String username, CheckoutForm form) {
        if (cartService.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng rỗng");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user: " + username));

        // Khởi tạo Order
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(form.getReceiverName());
        order.setReceiverPhone(form.getReceiverPhone());
        order.setReceiverAddress(form.getReceiverAddress());
        order.setNote(form.getNote());
        order.setPaymentMethod(form.getPaymentMethod());
        order.setStatus(OrderStatus.PENDING);          // hoặc DEFAULT trạng thái bạn định nghĩa
        order.setCreatedAt(LocalDateTime.now());

        // Tổng tiền
        BigDecimal total = BigDecimal.ZERO;

        // Thêm OrderItem từ giỏ
        for (CartItem ci : cartService.getItems()) {
            Product p = productRepository.findById(ci.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("SP không tồn tại: " + ci.getProduct().getId()));

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(p);
            oi.setPrice(p.getPrice());        // snapshot giá hiện tại
            oi.setQuantity(ci.getQty());

            // cộng dồn
            total = total.add(oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())));

            // gắn vào order
            order.getItems().add(oi);
        }

        order.setTotal(total);

        // Lưu đơn + items (cascade ở entity Order -> items)
        Order saved = orderRepository.save(order);

        // Xóa giỏ sau khi đặt
        cartService.clear();

        return saved.getId();
    }
}
