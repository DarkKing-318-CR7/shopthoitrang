package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.entity.User;
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
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String submit(
            @ModelAttribute("user") User user,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model
    ) {
        // 1. Kiểm tra username trùng
        if (userRepo.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại");
            return "register";
        }

        // 2. Kiểm tra email trùng
        if (user.getEmail() != null && userRepo.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email đã được sử dụng");
            return "register";
        }

        // 3. Kiểm tra confirm password
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp");
            return "register";
        }

        // 4. Mã hoá mật khẩu
        user.setPassword(encoder.encode(user.getPassword()));

        // 5. Set role mặc định
        // Nếu entity User dùng: private Role role;
        // Thì phải load Role từ DB (roleRepo.findByName("ROLE_USER"))
        // NHƯNG nếu bạn đang dùng String role thì:
        // user.setRole("ROLE_USER");

        user.setEnabled(true);

        // 6. Lưu user
        userRepo.save(user);

        model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        model.addAttribute("user", new User()); // clear form
        return "register";
    }
}
