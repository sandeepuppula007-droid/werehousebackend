package com.warehouse.warehouse_system.controller;

import com.warehouse.warehouse_system.entity.StockIn;
import com.warehouse.warehouse_system.entity.StockOut;
import com.warehouse.warehouse_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {

    @Autowired
    private StockService stockService;



    @PostMapping("/in")
    public ResponseEntity<StockIn> createStockIn(@RequestBody StockIn stockIn) {
        StockIn saved = stockService.createStockIn(stockIn);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/out")
    public ResponseEntity<StockOut> createStockOut(@RequestBody StockOut stockOut) {
        StockOut saved = stockService.createStockOut(stockOut);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/in/product/{productId}")
    public ResponseEntity<List<StockIn>> getStockInByProduct(@PathVariable Long productId) {
        List<StockIn> stockInList = stockService.getStockInByProductId(productId);
        return ResponseEntity.ok(stockInList);
    }

    @GetMapping("/out/product/{productId}")
    public ResponseEntity<List<StockOut>> getStockOutByProduct(@PathVariable Long productId) {
        List<StockOut> stockOutList = stockService.getStockOutByProductId(productId);
        return ResponseEntity.ok(stockOutList);
    }
    
    @GetMapping("/in")
    public ResponseEntity<List<StockIn>> getAllStockIn() {
        List<StockIn> stockInList = stockService.getAllStockIn();
        return ResponseEntity.ok(stockInList);
    }
    
    @GetMapping("/out")
    public ResponseEntity<List<StockOut>> getAllStockOut() {
        List<StockOut> stockOutList = stockService.getAllStockOut();
        return ResponseEntity.ok(stockOutList);
    }


}