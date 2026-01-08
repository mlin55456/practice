package com.example.inventory.controller;

import com.example.inventory.model.Product;
import com.example.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @GetMapping
    public List<Product> getAllProducts() {
        return inventoryService.getAllProducts();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = inventoryService.getProductById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        Optional<Product> product = inventoryService.getProductBySku(sku);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return inventoryService.saveProduct(product);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> existingProduct = inventoryService.getProductById(id);
        if (existingProduct.isPresent()) {
            product.setId(id);
            return ResponseEntity.ok(inventoryService.saveProduct(product));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = inventoryService.getProductById(id);
        if (product.isPresent()) {
            inventoryService.deleteProduct(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return inventoryService.searchProducts(keyword);
    }
    
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return inventoryService.getProductsByCategory(category);
    }
    
    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return inventoryService.getAllCategories();
    }
    
    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts() {
        return inventoryService.getLowStockProducts();
    }
}