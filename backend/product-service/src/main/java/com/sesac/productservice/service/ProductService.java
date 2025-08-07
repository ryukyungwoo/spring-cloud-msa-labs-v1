package com.sesac.productservice.service;

import com.sesac.productservice.entity.Product;
import com.sesac.productservice.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("user not found with id: " + id)
        );
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
