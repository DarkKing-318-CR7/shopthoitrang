package com.uth.shoptmdt.entity;

public enum OrderStatus {
    PENDING,     // chờ xác nhận
    CONFIRMED,   // shop đã xác nhận
    SHIPPING,    // đang giao
    COMPLETED,   // hoàn tất
    CANCELED     // hủy
}
