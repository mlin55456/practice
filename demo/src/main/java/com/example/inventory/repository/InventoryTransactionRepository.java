package com.example.inventory.repository;

import com.example.inventory.model.InventoryTransaction;
import com.example.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction> findByProduct(Product product);
    
    List<InventoryTransaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.transactionDate >= :date ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findTodayTransactions(@Param("date") LocalDateTime date);
}