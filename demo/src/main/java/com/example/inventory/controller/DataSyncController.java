package com.example.inventory.controller;

import com.example.inventory.service.DataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "*")
public class DataSyncController {
    
    @Autowired
    private DataSyncService dataSyncService;
    
    @PostMapping("/backup")
    public ResponseEntity<Map<String, String>> manualBackup() {
        try {
            dataSyncService.manualBackup();
            Map<String, String> response = new HashMap<>();
            response.put("message", "資料備份成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "資料備份失敗: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getBackupStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "正常運行");
        response.put("dailyBackup", "每日凌晨 2:00");
        response.put("incrementalBackup", "每小時執行");
        return ResponseEntity.ok(response);
    }
}