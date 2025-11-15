package com.uth.shoptmdt.service;

import com.uth.shoptmdt.entity.Product;
import com.uth.shoptmdt.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> search(Long categoryId, String keyword, Pageable pageable) {

        if (categoryId != null && keyword != null && !keyword.isEmpty()) {
            return productRepository
                    .findByCategory_IdAndNameContainingIgnoreCase(categoryId, keyword, pageable);
        }

        if (categoryId != null) {
            return productRepository.findByCategory_Id(categoryId, pageable);
        }

        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }

        return productRepository.findAll(pageable);
    }
}
