// src/main/java/.../controller/AccountController.java
package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.service.dto.PasswordForm;
import com.uth.shoptmdt.service.dto.ProfileForm;
import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.repository.OrderRepository;
import com.uth.shoptmdt.repository.UserRepository;
import com.uth.shoptmdt.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AccountService accountService;

    private User currentUser(UserDetails principal) {
        return userRepository.findTopByUsernameOrderByIdDesc(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user"));
    }

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = currentUser(principal);

        if (!model.containsAttribute("profileForm")) {
            ProfileForm pf = new ProfileForm();
            pf.setFullName(user.getFullName());
            pf.setEmail(user.getEmail());
            pf.setPhone(user.getPhone());
            pf.setAddressLine(user.getAddressLine());
            pf.setProvince(user.getProvince());
            pf.setDistrict(user.getDistrict());
            pf.setWard(user.getWard());
            model.addAttribute("profileForm", pf);
        }
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new PasswordForm());
        }

        model.addAttribute("user", user);
        model.addAttribute("orders",
                orderRepository.findByUser_UsernameOrderByCreatedAtDesc(user.getUsername()));

        return "account"; // templates/account.html
    }

    @PostMapping("/account/profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails principal,
                                @Valid ProfileForm profileForm,
                                BindingResult br,
                                RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", br);
            ra.addFlashAttribute("profileForm", profileForm);
            ra.addFlashAttribute("tab", "profile");
            return "redirect:/account";
        }
        accountService.updateProfile(currentUser(principal), profileForm);
        ra.addFlashAttribute("success", "Cập nhật thông tin thành công");
        ra.addFlashAttribute("tab", "profile");
        return "redirect:/account";
    }

    @PostMapping("/account/password")
    public String changePassword(@AuthenticationPrincipal UserDetails principal,
                                 @Valid PasswordForm passwordForm,
                                 BindingResult br,
                                 RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.passwordForm", br);
            ra.addFlashAttribute("passwordForm", passwordForm);
            ra.addFlashAttribute("tab", "password");
            return "redirect:/account";
        }
        try {
            accountService.changePassword(currentUser(principal), passwordForm);
            ra.addFlashAttribute("success", "Đổi mật khẩu thành công");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            ra.addFlashAttribute("passwordForm", passwordForm);
            ra.addFlashAttribute("tab", "password");
        }
        return "redirect:/account";
    }
}
