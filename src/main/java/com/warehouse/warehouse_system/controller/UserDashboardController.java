package com.warehouse.warehouse_system.controller;

import com.warehouse.warehouse_system.service.ProductService;
import com.warehouse.warehouse_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserDashboardController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private StockService stockService;

    @GetMapping("/products")
    public Map<String, Object> getUserProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "") String search) {
        return productService.getProductsWithStatsPaginated(page, size, search);
    }

    @GetMapping("/stats")
    public Map<String, Object> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        
        int totalIn = stockService.getAllStockIn().stream()
                .mapToInt(s -> s.getQuantity()).sum();
        int totalOut = stockService.getAllStockOut().stream()
                .mapToInt(s -> s.getQuantity()).sum();
        
        List<Map<String, Object>> productsWithStats = productService.getProductsWithStats();
        long totalProducts = productsWithStats.size();
        long lowStockItems = productsWithStats.stream()
                .filter(p -> (Integer) p.get("currentStock") <= 5)
                .count();
        
        stats.put("totalStockIn", totalIn);
        stats.put("totalStockOut", totalOut);
        stats.put("totalAvailable", totalIn - totalOut);
        stats.put("totalProducts", totalProducts);
        stats.put("lowStockItems", lowStockItems);
        
        return stats;
    }

    @GetMapping("/chart-data")
    public Map<String, Object> getChartData() {
        Map<String, Object> chartData = new HashMap<>();
        
        int totalIn = stockService.getAllStockIn().stream()
                .mapToInt(s -> s.getQuantity()).sum();
        int totalOut = stockService.getAllStockOut().stream()
                .mapToInt(s -> s.getQuantity()).sum();
        
        chartData.put("stockInVsOut", Map.of(
            "labels", List.of("Stock In", "Stock Out"),
            "data", List.of(totalIn, totalOut)
        ));
        
        List<Map<String, Object>> products = productService.getProductsWithStats();
        List<String> productNames = products.stream()
                .map(p -> (String) p.get("name"))
                .limit(5)
                .toList();
        List<Integer> availableStock = products.stream()
                .map(p -> (Integer) p.get("currentStock"))
                .limit(5)
                .toList();
        
        chartData.put("availableStock", Map.of(
            "labels", productNames,
            "data", availableStock
        ));
        
        return chartData;
    }
}