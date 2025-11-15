package com.uth.shoptmdt.config;

import com.uth.shoptmdt.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalCartAdvice {

    private final CartService cart;

    @ModelAttribute("totalQtyHeader")
    public int totalQtyHeader() {
        return cart.getTotalQty();
    }

    @ModelAttribute("totalAmountHeader")
    public BigDecimal totalAmountHeader() {
        return cart.getTotalAmount();
    }

    @ModelAttribute("itemsHeader")
    public Object itemsHeader() {
        return cart.getItems();
    }
}
