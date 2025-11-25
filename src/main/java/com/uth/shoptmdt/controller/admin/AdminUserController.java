package com.uth.shoptmdt.controller.admin;

import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // LIST
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("pageTitle", "Quản lý người dùng");
        model.addAttribute("users", userService.findAll());
        return "admin/user-list";
    }

    // CREATE FORM
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        User u = new User();
        u.setEnabled(true); // mặc định active
        model.addAttribute("user", u);
        model.addAttribute("pageTitle", "Thêm người dùng");
        return "admin/user-form";
    }

    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User u = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user id=" + id));

        model.addAttribute("user", u);
        model.addAttribute("pageTitle", "Sửa người dùng");
        return "admin/user-form";
    }

    // SAVE
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {

        // ==== CASE: EDIT USER ====
        if (user.getId() != null) {
            User existing = userService.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user id=" + user.getId()));

            // Nếu password từ form để trống → giữ nguyên password cũ
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                user.setPassword(existing.getPassword());
            }
        }

        // ==== LƯU (UserService sẽ tự encode password nếu cần) ====
        userService.save(user);

        return "redirect:/admin/users?success";
    }


    // ENABLE / DISABLE
    @PostMapping("/toggle/{id}")
    public String toggleUser(@PathVariable Long id) {
        User u = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user id=" + id));
        u.setEnabled(!u.isEnabled());
        userService.save(u);
        return "redirect:/admin/users?updated";
    }

    // DELETE
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users?deleted";
    }
}
