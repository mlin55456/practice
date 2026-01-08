package com.example.inventory.config;

import com.example.inventory.model.Product;
import com.example.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 如果資料庫是空的，初始化一些範例資料
        if (productRepository.count() == 0) {
            initializeSampleData();
        }
    }
    
    private void initializeSampleData() {
        // 建立範例產品
        Product[] sampleProducts = {
            new Product("筆記型電腦", "LAPTOP001", "高效能商務筆記型電腦", 15, 35000.0, "電子產品"),
            new Product("無線滑鼠", "MOUSE001", "人體工學無線滑鼠", 50, 800.0, "電子產品"),
            new Product("機械鍵盤", "KEYBOARD001", "RGB背光機械鍵盤", 25, 2500.0, "電子產品"),
            new Product("辦公椅", "CHAIR001", "人體工學辦公椅", 8, 4500.0, "辦公用品"),
            new Product("辦公桌", "DESK001", "升降辦公桌", 5, 8000.0, "辦公用品"),
            new Product("印表機", "PRINTER001", "多功能雷射印表機", 3, 12000.0, "辦公設備"),
            new Product("投影機", "PROJECTOR001", "4K商務投影機", 2, 25000.0, "辦公設備"),
            new Product("白板", "BOARD001", "磁性白板", 12, 1200.0, "辦公用品"),
            new Product("文件夾", "FOLDER001", "A4文件夾", 100, 25.0, "文具用品"),
            new Product("原子筆", "PEN001", "藍色原子筆", 200, 10.0, "文具用品")
        };
        
        for (Product product : sampleProducts) {
            productRepository.save(product);
        }
        
        System.out.println("已初始化 " + sampleProducts.length + " 個範例產品");
    }
}