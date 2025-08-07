package com.sesac.productservice.controller;

import com.sesac.productservice.entity.Product;
import com.sesac.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "get Product", description = "find Product by id")
    public ResponseEntity<List<Product>> getProducts() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
        }  catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "get Product", description = "find Product by id")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id));
        }  catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
