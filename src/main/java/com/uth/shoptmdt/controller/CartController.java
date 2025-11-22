package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.repository.UserRepository;
import com.uth.shoptmdt.service.CartService;
import com.uth.shoptmdt.entity.Product;
import com.uth.shoptmdt.service.CheckoutForm;
import com.uth.shoptmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cart;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {

        // nếu chưa có thì mới tạo (để giữ lại dữ liệu khi validate lỗi)
        if (!model.containsAttribute("checkoutForm")) {
            CheckoutForm form = new CheckoutForm();

            if (principal != null) {
                userRepository.findByUsername(principal.getName())
                        .ifPresent(u -> {
                            form.setFullName(u.getFullName());
                            form.setPhone(u.getPhone());
                            form.setAddressLine(u.getAddressLine());
                            form.setProvince(u.getProvince());
                            form.setDistrict(u.getDistrict());
                            form.setWard(u.getWard());
                        });
            }

            model.addAttribute("checkoutForm", form);
        }

        model.addAttribute("items", cartService.getItems());
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        return "shoping-cart";
    }


    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @RequestParam(defaultValue = "1") int qty,
                            @RequestParam(required = false) String redirect) {
        Product p = productService.findById(id);
        if (p != null) cart.add(p, qty);
        return (redirect != null && !redirect.isBlank()) ? "redirect:" + redirect : "redirect:/cart";
    }

    @PostMapping("/cart/update/{id}")
    public String updateQty(@PathVariable Long id, @RequestParam int qty,
                            @RequestParam(required = false) String redirect) {
        cart.updateQty(id, qty);
        return (redirect != null && !redirect.isBlank()) ? "redirect:" + redirect : "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String remove(@PathVariable Long id,
                         @RequestParam(required = false) String redirect) {
        cart.remove(id);
        return (redirect != null && !redirect.isBlank()) ? "redirect:" + redirect : "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clear(@RequestParam(required = false) String redirect) {
        cart.clear();
        return (redirect != null && !redirect.isBlank()) ? "redirect:" + redirect : "redirect:/cart";
    }

}
