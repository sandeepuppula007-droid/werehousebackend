package com.warehouse.warehouse_system.repository;

import com.warehouse.warehouse_system.entity.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockInRepository extends JpaRepository<StockIn, Integer> {
    List<StockIn> findByProductIdOrderByInDateDesc(Long productId);
    
    @Query("SELECT SUM(s.quantity) FROM StockIn s WHERE s.productId = ?1")
    Integer getTotalStockInByProductId(int productId);
    
    @Query("SELECT si.id, p.name, si.quantity, si.receivedFrom, si.inDate " +
           "FROM StockIn si JOIN Product p ON si.productId = p.productId " +
           "WHERE (:productId IS NULL OR si.productId = :productId) " +
           "AND (:startDate IS NULL OR si.inDate >= :startDate) " +
           "AND (:endDate IS NULL OR si.inDate <= :endDate) " +
           "ORDER BY si.inDate DESC")
    List<Object[]> findStockInWithProductName(@Param("productId") Long productId, 
                                              @Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
}