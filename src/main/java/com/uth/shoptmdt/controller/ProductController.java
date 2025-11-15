package com.uth.shoptmdt.controller;

import com.uth.shoptmdt.entity.Product;
import com.uth.shoptmdt.service.CategoryService;
import com.uth.shoptmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String listProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            Model model) {

        String[] parts = sort.split(",");
        Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));

        Page<Product> products = productService.search(categoryId, keyword, pageable);

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);

        return "product";
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
