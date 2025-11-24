package com.uth.shoptmdt.controller.admin;

import com.uth.shoptmdt.entity.Product;
import com.uth.shoptmdt.service.CategoryService;
import com.uth.shoptmdt.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // LIST
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("pageTitle", "Quản lý sản phẩm");
        model.addAttribute("products", productService.findAll());
        return "admin/product-list";
    }

    // CREATE FORM
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Product p = new Product();
        // set mặc định nếu cần
        p.setPrice(BigDecimal.ZERO);
        model.addAttribute("product", p);
        model.addAttribute("pageTitle", "Thêm sản phẩm");
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    // EDIT FORM
// EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product p = productService.findById(id); // trả về Product

        if (p == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm với id: " + id);
        }

        model.addAttribute("product", p);
        model.addAttribute("pageTitle", "Sửa sản phẩm (ID: " + id + ")");
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }


    // SAVE (create + update)
    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute("product") @Valid Product product,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", (product.getId() == null) ? "Thêm sản phẩm" : "Sửa sản phẩm");
            return "admin/product-form";
        }

        // nếu bạn có logic riêng (ví dụ price null) thì xử lý thêm ở đây
        productService.save(product);
        return "redirect:/admin/products?success";
    }

    // DELETE
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products?deleted";
    }
}
