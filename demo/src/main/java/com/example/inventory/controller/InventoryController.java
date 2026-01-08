package com.example.inventory.controller;

import com.example.inventory.model.InventoryTransaction;
import com.example.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @PostMapping("/add-stock")
    public ResponseEntity<InventoryTransaction> addStock(@RequestBody Map<String, Object> request) {
        try {
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            String reason = request.get("reason").toString();
            
            InventoryTransaction transaction = inventoryService.addStock(productId, quantity, reason);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/remove-stock")
    public ResponseEntity<InventoryTransaction> removeStock(@RequestBody Map<String, Object> request) {
        try {
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            String reason = request.get("reason").toString();
            
            InventoryTransaction transaction = inventoryService.removeStock(productId, quantity, reason);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/adjust-stock")
    public ResponseEntity<InventoryTransaction> adjustStock(@RequestBody Map<String, Object> request) {
        try {
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer newQuantity = Integer.valueOf(request.get("newQuantity").toString());
            String reason = request.get("reason").toString();
            
            InventoryTransaction transaction = inventoryService.adjustStock(productId, newQuantity, reason);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/transactions/product/{productId}")
    public List<InventoryTransaction> getProductTransactions(@PathVariable Long productId) {
        return inventoryService.getProductTransactions(productId);
    }
    
    @GetMapping("/transactions/today")
    public List<InventoryTransaction> getTodayTransactions() {
        return inventoryService.getTodayTransactions();
    }
    
    @GetMapping("/transactions/range")
    public List<InventoryTransaction> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return inventoryService.getTransactionsByDateRange(start, end);
    }
}