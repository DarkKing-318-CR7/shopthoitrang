package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.service.CategoryService;
import com.uth.shoptmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // Danh sách tất cả sản phẩm
    @GetMapping("/products")
    public String listProducts(Model model,
                               @RequestParam(value = "keyword", required = false) String keyword) {

        if (keyword != null && !keyword.isBlank()) {
            // nếu sau này bạn muốn tìm kiếm, có thể dùng findByNameContaining
            model.addAttribute("products", productService.findAll()); // tạm thời cho all
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("products", productService.findAll());
        }

        model.addAttribute("categories", categoryService.findAll());

        return "product"; // templates/product.html
    }

    // Lọc theo danh mục
    @GetMapping("/products/category/{id}")
    public String listProductsByCategory(@PathVariable("id") Long categoryId, Model model) {
        model.addAttribute("products", productService.findByCategory(categoryId));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        return "product";
    }

    // Chi tiết sản phẩm
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        var product = productService.findById(id);
        if (product == null) {
            return "redirect:/products"; // nếu không tìm thấy thì quay lại list
        }
        model.addAttribute("product", product);
        return "product-detail"; // templates/product-detail.html
    }
}
