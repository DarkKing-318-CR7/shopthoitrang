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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final UserRepository userRepo;

    @GetMapping("/checkout")
    public String showCheckout(Model model, Principal principal) {
        var items = cartService.getItems();
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.getTotalAmount());

        if (!model.containsAttribute("form")) {
            CheckoutForm f = new CheckoutForm();
            f.setPaymentMethod("COD");

            // Prefill từ hồ sơ user
            userRepo.findByUsername(principal.getName()).ifPresent(u -> {
                if (f.getReceiverName() == null || f.getReceiverName().isBlank()) {
                    f.setReceiverName(u.getFullName());
                }
                if (f.getReceiverPhone() == null || f.getReceiverPhone().isBlank()) {
                    f.setReceiverPhone(u.getPhone());
                }
                if (f.getReceiverAddress() == null || f.getReceiverAddress().isBlank()) {
                    f.setReceiverAddress(u.getAddress());
                }
            });

            model.addAttribute("form", f);
        }
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@Valid @ModelAttribute("form") CheckoutForm form,
                             BindingResult br,
                             Principal principal,
                             Model model,
                             RedirectAttributes ra) {
        if (br.hasErrors()) {
            var items = cartService.getItems();
            model.addAttribute("items", items);
            model.addAttribute("total", cartService.getTotalAmount());
            return "checkout";
        }
        Long orderId = checkoutService.placeOrder(principal.getName(), form);
        ra.addFlashAttribute("orderId", orderId);
        return "redirect:/order-success";
    }
}
