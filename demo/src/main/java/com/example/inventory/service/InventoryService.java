package com.example.inventory.service;

import com.example.inventory.model.InventoryTransaction;
import com.example.inventory.model.Product;
import com.example.inventory.repository.InventoryTransactionRepository;
import com.example.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private InventoryTransactionRepository transactionRepository;
    
    // 產品管理
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }
    
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
    
    // 庫存交易管理
    public InventoryTransaction addStock(Long productId, Integer quantity, String reason) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("產品不存在"));
        
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
        
        InventoryTransaction transaction = new InventoryTransaction(
            product, InventoryTransaction.TransactionType.IN, quantity, reason);
        return transactionRepository.save(transaction);
    }
    
    public InventoryTransaction removeStock(Long productId, Integer quantity, String reason) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("產品不存在"));
        
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("庫存不足");
        }
        
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        
        InventoryTransaction transaction = new InventoryTransaction(
            product, InventoryTransaction.TransactionType.OUT, quantity, reason);
        return transactionRepository.save(transaction);
    }
    
    public InventoryTransaction adjustStock(Long productId, Integer newQuantity, String reason) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("產品不存在"));
        
        Integer adjustment = newQuantity - product.getQuantity();
        product.setQuantity(newQuantity);
        productRepository.save(product);
        
        InventoryTransaction transaction = new InventoryTransaction(
            product, InventoryTransaction.TransactionType.ADJUST, adjustment, reason);
        return transactionRepository.save(transaction);
    }
    
    public List<InventoryTransaction> getProductTransactions(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("產品不存在"));
        return transactionRepository.findByProduct(product);
    }
    
    public List<InventoryTransaction> getTodayTransactions() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return transactionRepository.findTodayTransactions(startOfDay);
    }
    
    public List<InventoryTransaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTransactionDateBetween(start, end);
    }
}