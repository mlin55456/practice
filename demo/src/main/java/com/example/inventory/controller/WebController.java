package com.example.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/products")
    public String products() {
        return "products";
    }
    
    @GetMapping("/transactions")
    public String transactions() {
        return "transactions";
    }
    
    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }
}