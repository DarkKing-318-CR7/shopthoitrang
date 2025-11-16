// src/main/java/com/uth/shoptmdt/controller/CheckoutController.java
package com.uth.shoptmdt.controller;

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

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final CheckoutService checkoutService;

    @PostMapping("/checkout")
    public String placeOrder(@Valid CheckoutForm form,
                             BindingResult br,
                             Principal principal,
                             RedirectAttributes ra,
                             Model model) {
        if (br.hasErrors()) {
            // nếu validate fail thì nạp lại dữ liệu giỏ để render lại trang /cart
            model.addAttribute("items", cartService.getItems());
            model.addAttribute("totalAmount", cartService.getTotalAmount());
            ra.addFlashAttribute("formError", "Vui lòng kiểm tra thông tin.");
            return "shoping-cart"; // hoặc redirect tới /cart tuỳ bạn đang render
        }

        Long orderId = checkoutService.placeOrder(principal.getName(), form);
        ra.addFlashAttribute("orderId", orderId);
        return "redirect:/order-success?id=" + orderId;
    }
}
