package com.warehouse.warehouse_system.service.impl;

import com.warehouse.warehouse_system.entity.StockIn;
import com.warehouse.warehouse_system.entity.StockOut;
import com.warehouse.warehouse_system.repository.StockInRepository;
import com.warehouse.warehouse_system.repository.StockOutRepository;
import com.warehouse.warehouse_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    
    @Autowired
    private StockInRepository stockInRepository;
    
    @Autowired
    private StockOutRepository stockOutRepository;
    
    @Override
    public List<StockIn> getAllStockIn() {
        return stockInRepository.findAll();
    }
    
    @Override
    public List<StockOut> getAllStockOut() {
        return stockOutRepository.findAll();
    }
    

    
    @Override
    public StockIn createStockIn(StockIn stockIn) {
        stockIn.setInDate(LocalDateTime.now());
        return stockInRepository.save(stockIn);
    }
    
    @Override
    public StockOut createStockOut(StockOut stockOut) {
        stockOut.setOutDate(LocalDateTime.now());
        return stockOutRepository.save(stockOut);
    }
    
    @Override
    public List<StockIn> getStockInByProductId(Long productId) {
        return stockInRepository.findByProductIdOrderByInDateDesc(productId);
    }
    
    @Override
    public List<StockOut> getStockOutByProductId(Long productId) {
        return stockOutRepository.findByProductIdOrderByOutDateDesc(productId);
    }
    

}