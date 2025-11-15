package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.service.CartService;
import com.uth.shoptmdt.entity.Product;
import com.uth.shoptmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cart;
    private final ProductService productService;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("items", cart.getItems());
        model.addAttribute("totalQty", cart.getTotalQty());
        model.addAttribute("totalAmount", cart.getTotalAmount());
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
