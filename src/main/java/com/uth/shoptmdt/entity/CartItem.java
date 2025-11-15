package com.uth.shoptmdt.entity;

import java.math.BigDecimal;

public class CartItem {
    private final Product product;
    private int qty;

    public CartItem(Product product, int qty) {
        this.product = product;
        this.qty = qty;
    }

    public Product getProduct() { return product; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = Math.max(1, qty); }

    public BigDecimal getUnitPrice() {
        return product.getPrice(); // kiểu BigDecimal trong Product
    }
    public BigDecimal getTotal() {
        return getUnitPrice().multiply(BigDecimal.valueOf(qty));
    }
}
