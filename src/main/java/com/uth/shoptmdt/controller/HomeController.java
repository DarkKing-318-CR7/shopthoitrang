package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.repository.OrderRepository;
import com.uth.shoptmdt.repository.UserRepository;
import com.uth.shoptmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.findAll());
        return "index"; // trả về templates/index.html
    }

//    @GetMapping("/blog")
//    public String blog(){
//        return "blog";
//    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }
//    @GetMapping("/blog-detail")
//    public String blog_detail(){
//        return "blog-detail";
//    }




}
