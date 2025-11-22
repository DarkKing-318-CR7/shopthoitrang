// src/main/java/com/uth/shoptmdt/controller/BlogController.java
package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/blog")
    public String blogList(Model model) {
        model.addAttribute("posts", blogService.findAll());
        return "blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetail(@PathVariable Long id, Model model) {
        var post = blogService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        model.addAttribute("post", post);
        return "blog-detail";
    }

}
