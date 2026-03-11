package com.warehouse.warehouse_system.service;

import com.warehouse.warehouse_system.entity.StockIn;
import com.warehouse.warehouse_system.entity.StockOut;
import java.util.List;

public interface StockService {
    List<StockIn> getAllStockIn();
    List<StockOut> getAllStockOut();
    StockIn createStockIn(StockIn stockIn);
    StockOut createStockOut(StockOut stockOut);
    List<StockIn> getStockInByProductId(Long productId);
    List<StockOut> getStockOutByProductId(Long productId);
}