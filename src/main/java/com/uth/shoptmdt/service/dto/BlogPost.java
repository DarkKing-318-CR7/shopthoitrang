package com.uth.shoptmdt.service.dto;
// src/main/java/com/uth/shoptmdt/dto/BlogPost.java

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BlogPost {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String image;    // tên file ảnh: blog-04.jpg...
    private LocalDate date;  // ngày đăng
    private String category; // Thể loại
}
