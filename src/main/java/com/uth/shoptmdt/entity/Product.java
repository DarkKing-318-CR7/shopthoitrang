package com.uth.shoptmdt.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    private Double price;

    @Column(length = 2000)
    private String description;

    @Column(length = 255)
    private String image; // tên file ảnh, ví dụ: "product-01.jpg"

    private Integer quantity; // tồn kho

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
