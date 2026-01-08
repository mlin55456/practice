package com.example.inventory.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false)
    private Integer quantity;
    
    private String reason;
    
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    
    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }
    
    public enum TransactionType {
        IN,    // 入庫
        OUT,   // 出庫
        ADJUST // 調整
    }
    
    // Constructors
    public InventoryTransaction() {}
    
    public InventoryTransaction(Product product, TransactionType type, Integer quantity, String reason) {
        this.product = product;
        this.type = type;
        this.quantity = quantity;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
}