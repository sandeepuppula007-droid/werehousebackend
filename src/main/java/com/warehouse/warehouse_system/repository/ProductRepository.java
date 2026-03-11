package com.warehouse.warehouse_system.repository;

import com.warehouse.warehouse_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByWarehouseId(Long warehouseId);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT p.productId, p.name, " +
           "COALESCE(SUM(si.quantity), 0) as totalIn, " +
           "COALESCE(SUM(so.quantity), 0) as totalOut, " +
           "(COALESCE(SUM(si.quantity), 0) - COALESCE(SUM(so.quantity), 0)) as currentStock " +
           "FROM Product p " +
           "LEFT JOIN StockIn si ON p.productId = si.productId " +
           "LEFT JOIN StockOut so ON p.productId = so.productId " +
           "GROUP BY p.productId, p.name " +
           "ORDER BY p.name")
    List<Object[]> findProductsWithStockSummary();
}