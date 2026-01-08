package com.example.inventory.service;

import com.example.inventory.model.InventoryTransaction;
import com.example.inventory.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DataSyncService {
    
    @Autowired
    private InventoryService inventoryService;
    
    private static final String BACKUP_DIR = "data/backup/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 每天凌晨 2 點執行資料備份
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyDataBackup() {
        try {
            String today = LocalDateTime.now().format(DATE_FORMAT);
            backupProducts(today);
            backupTransactions(today);
            System.out.println("每日資料備份完成: " + today);
        } catch (Exception e) {
            System.err.println("資料備份失敗: " + e.getMessage());
        }
    }
    
    // 每小時執行一次增量備份
    @Scheduled(fixedRate = 3600000) // 1 hour = 3600000 ms
    public void hourlyIncrementalBackup() {
        try {
            List<InventoryTransaction> todayTransactions = inventoryService.getTodayTransactions();
            if (!todayTransactions.isEmpty()) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH"));
                backupIncrementalTransactions(todayTransactions, timestamp);
                System.out.println("增量備份完成: " + timestamp);
            }
        } catch (Exception e) {
            System.err.println("增量備份失敗: " + e.getMessage());
        }
    }
    
    private void backupProducts(String date) throws IOException {
        List<Product> products = inventoryService.getAllProducts();
        String filename = BACKUP_DIR + "products_" + date + ".csv";
        
        // 確保目錄存在
        java.io.File dir = new java.io.File(BACKUP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        try (FileWriter writer = new FileWriter(filename)) {
            // CSV 標題
            writer.append("ID,SKU,名稱,描述,數量,價格,類別,建立時間,更新時間\n");
            
            for (Product product : products) {
                writer.append(String.valueOf(product.getId())).append(",");
                writer.append(escapeCSV(product.getSku())).append(",");
                writer.append(escapeCSV(product.getName())).append(",");
                writer.append(escapeCSV(product.getDescription())).append(",");
                writer.append(String.valueOf(product.getQuantity())).append(",");
                writer.append(String.valueOf(product.getPrice())).append(",");
                writer.append(escapeCSV(product.getCategory())).append(",");
                writer.append(product.getCreatedAt().format(DATETIME_FORMAT)).append(",");
                writer.append(product.getUpdatedAt().format(DATETIME_FORMAT)).append("\n");
            }
        }
    }
    
    private void backupTransactions(String date) throws IOException {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        List<InventoryTransaction> transactions = inventoryService.getTransactionsByDateRange(startOfDay, endOfDay);
        String filename = BACKUP_DIR + "transactions_" + date + ".csv";
        
        try (FileWriter writer = new FileWriter(filename)) {
            // CSV 標題
            writer.append("ID,產品ID,產品SKU,交易類型,數量,原因,交易時間\n");
            
            for (InventoryTransaction transaction : transactions) {
                writer.append(String.valueOf(transaction.getId())).append(",");
                writer.append(String.valueOf(transaction.getProduct().getId())).append(",");
                writer.append(escapeCSV(transaction.getProduct().getSku())).append(",");
                writer.append(transaction.getType().toString()).append(",");
                writer.append(String.valueOf(transaction.getQuantity())).append(",");
                writer.append(escapeCSV(transaction.getReason())).append(",");
                writer.append(transaction.getTransactionDate().format(DATETIME_FORMAT)).append("\n");
            }
        }
    }
    
    private void backupIncrementalTransactions(List<InventoryTransaction> transactions, String timestamp) throws IOException {
        String filename = BACKUP_DIR + "incremental_" + timestamp + ".csv";
        
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("ID,產品ID,產品SKU,交易類型,數量,原因,交易時間\n");
            
            for (InventoryTransaction transaction : transactions) {
                writer.append(String.valueOf(transaction.getId())).append(",");
                writer.append(String.valueOf(transaction.getProduct().getId())).append(",");
                writer.append(escapeCSV(transaction.getProduct().getSku())).append(",");
                writer.append(transaction.getType().toString()).append(",");
                writer.append(String.valueOf(transaction.getQuantity())).append(",");
                writer.append(escapeCSV(transaction.getReason())).append(",");
                writer.append(transaction.getTransactionDate().format(DATETIME_FORMAT)).append("\n");
            }
        }
    }
    
    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    // 手動觸發備份的方法
    public void manualBackup() {
        dailyDataBackup();
    }
}