package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.entity.Role;                    // +++
import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.repository.RoleRepository;      // +++
import com.uth.shoptmdt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;              // +++
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String submit(@ModelAttribute("user") User u, Model model) {
        if (userRepo.existsByUsername(u.getUsername())) {
            model.addAttribute("error", "Username đã tồn tại");
            return "register";
        }

        u.setPassword(encoder.encode(u.getPassword()));

        // CHỈNH 1: thay vì getRole()/setRole(String)
        Role defaultRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Thiếu ROLE_USER trong bảng roles"));
        u.setRole(defaultRole);                         // CHỈNH 2: dùng setRoles(Role)

        // CHỈNH 3: enabled là boolean -> gán trực tiếp, không kiểm null
        u.setEnabled(true);

        userRepo.save(u);
        return "redirect:/login?registered";
    }
}
