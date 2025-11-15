package com.uth.shoptmdt.service;

import com.uth.shoptmdt.entity.CartItem;
import com.uth.shoptmdt.entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@SessionScope
public class CartService {

    // Giới hạn số lượng mỗi sản phẩm trong giỏ
    private static final int MIN_QTY = 1;
    private static final int MAX_QTY = 999;

    // Lưu trữ theo thứ tự thêm vào
    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    /* ===================== Core APIs ===================== */

    /** Thêm sản phẩm vào giỏ (tự động +qty nếu đã tồn tại) */
    public void add(Product p, int qty) {
        if (p == null || p.getId() == null) return;
        int addQty = sanitizeQty(qty);
        CartItem ex = items.get(p.getId());
        if (ex == null) {
            items.put(p.getId(), new CartItem(p, addQty));
        } else {
            ex.setQty(boundQty(ex.getQty() + addQty));
        }
    }

    /** Tăng 1 */
    public void inc(Long productId) {
        changeBy(productId, +1);
    }

    /** Giảm 1 (<=0 thì xóa) */
    public void dec(Long productId) {
        changeBy(productId, -1);
    }

    /** Set số lượng tuyệt đối (<=0 thì xóa) */
    public void setQty(Long productId, int qty) {
        CartItem it = items.get(productId);
        if (it == null) return;
        if (qty <= 0) {
            items.remove(productId);
        } else {
            it.setQty(boundQty(qty));
        }
    }

    /** Xóa 1 sản phẩm khỏi giỏ */
    public void remove(Long productId) {
        items.remove(productId);
    }

    /** Xóa toàn bộ giỏ */
    public void clear() {
        items.clear();
    }

    /* ===================== Queries ===================== */

    /** Danh sách item (mutable). Dùng cho render nhanh. */
    public Collection<CartItem> getItems() {
        return items.values();
    }

    /** Bản sao “đóng băng” để tránh sửa ngoài ý muốn (an toàn cho xử lý nghiệp vụ) */
    public List<CartItem> snapshot() {
        return items.values().stream()
                .map(ci -> new CartItem(ci.getProduct(), ci.getQty()))
                .collect(Collectors.toUnmodifiableList());
    }

    /** Tổng số dòng (sản phẩm khác nhau) */
    public int size() {
        return items.size();
    }

    /** Giỏ trống? */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Tổng số lượng (cộng tất cả dòng) */
    public int getTotalQty() {
        return items.values().stream().mapToInt(CartItem::getQty).sum();
    }

    /** Tổng tiền (BigDecimal an toàn) */
    public BigDecimal getTotalAmount() {
        return items.values().stream()
                .map(CartItem::getTotal)                   // CartItem#getTotal đã nhân price*qty
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /* ===================== Helpers ===================== */

    private void changeBy(Long productId, int delta) {
        CartItem it = items.get(productId);
        if (it == null) return;
        int next = it.getQty() + delta;
        if (next <= 0) {
            items.remove(productId);
        } else {
            it.setQty(boundQty(next));
        }
    }

    private int sanitizeQty(int qty) {
        return qty <= 0 ? MIN_QTY : boundQty(qty);
    }

    private int boundQty(int qty) {
        if (qty < MIN_QTY) return MIN_QTY;
        if (qty > MAX_QTY) return MAX_QTY;
        return qty;
    }

    public void updateQty(Long productId, int qty) {
        CartItem it = items.get(productId);
        if (it != null) it.setQty(Math.max(1, qty));
    }
}
