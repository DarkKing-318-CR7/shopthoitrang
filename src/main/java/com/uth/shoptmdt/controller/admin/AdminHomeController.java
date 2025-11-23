package com.uth.shoptmdt.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class AdminHomeController {

    @GetMapping("/admin")
    public String adminHome(Model model) {
        model.addAttribute("pageTitle", "Trang quản trị");
        return "admin/dashboard";
    }
}
