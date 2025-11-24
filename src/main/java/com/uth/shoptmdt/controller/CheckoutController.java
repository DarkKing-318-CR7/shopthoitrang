// src/main/java/com/uth/shoptmdt/controller/CheckoutController.java
package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.repository.UserRepository;
import com.uth.shoptmdt.service.CheckoutForm;
import com.uth.shoptmdt.service.CartService;
import com.uth.shoptmdt.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final UserRepository userRepository;

    // ================================
    // 1) Xử lý thanh toán
    // ================================
    // src/main/java/com/uth/shoptmdt/controller/CheckoutController.java
    @PostMapping("/checkout")
    public String placeOrder(@Valid @ModelAttribute("checkoutForm") CheckoutForm form,
                             BindingResult br,
                             Principal principal,
                             @RequestParam(value = "action", required = false) String action,
                             RedirectAttributes ra,
                             Model model) {

        // Nếu bấm "Áp dụng" mã giảm giá
        if ("apply".equals(action)) {
            // Không cần validate full thông tin, chỉ cần giỏ + promoCode
            var items = cartService.getItems();
            var total = cartService.getTotalAmount();

            // Logic giảm giá tạm thời: GIAM10 / GIAM20
            BigDecimal discount = BigDecimal.ZERO;
            if (form.getPromoCode() != null) {
                String code = form.getPromoCode().trim().toUpperCase();
                if ("GIAM10".equals(code)) {
                    discount = total.multiply(BigDecimal.valueOf(0.10));
                } else if ("GIAM20".equals(code)) {
                    discount = total.multiply(BigDecimal.valueOf(0.20));
                }
            }
            BigDecimal finalAmount = total.subtract(discount);

            model.addAttribute("checkoutForm", form);
            model.addAttribute("items", items);
            model.addAttribute("totalAmount", total);
            model.addAttribute("appliedDiscount", discount);
            model.addAttribute("finalAmount", finalAmount);

            return "shoping-cart";
        }

        // Nếu bấm "Thanh toán" → tạo đơn
        if (br.hasErrors()) {
            model.addAttribute("items", cartService.getItems());
            BigDecimal total = cartService.getTotalAmount();

            model.addAttribute("totalAmount", total);
            model.addAttribute("appliedDiscount", BigDecimal.ZERO);
            model.addAttribute("finalAmount", total);
            ra.addFlashAttribute("formError", "Vui lòng kiểm tra thông tin.");
            return "shoping-cart";
        }

        Long orderId = checkoutService.placeOrder(principal.getName(), form);
        ra.addFlashAttribute("orderId", orderId);
        return "redirect:/order-success?id=" + orderId;
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model, Principal principal) {

        CheckoutForm form = new CheckoutForm();

        if (principal != null) {
            userRepository.findByUsername(principal.getName())
                    .ifPresent(user -> {
                        form.setFullName(user.getFullName());
                        form.setPhone(user.getPhone());
                        form.setAddressLine(user.getAddressLine());
                        form.setProvince(user.getProvince());
                        form.setDistrict(user.getDistrict());
                        form.setWard(user.getWard());
                    });
        }

        var total = cartService.getTotalAmount();

        model.addAttribute("checkoutForm", form);
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("totalAmount", total);
        model.addAttribute("appliedDiscount", BigDecimal.ZERO);
        model.addAttribute("finalAmount", total);

        return "shoping-cart";
    }


}
