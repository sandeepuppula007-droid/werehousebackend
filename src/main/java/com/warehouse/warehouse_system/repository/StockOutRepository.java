package com.warehouse.warehouse_system.repository;

import com.warehouse.warehouse_system.entity.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockOutRepository extends JpaRepository<StockOut, Integer> {
    List<StockOut> findByProductIdOrderByOutDateDesc(Long productId);
    
    @Query("SELECT SUM(s.quantity) FROM StockOut s WHERE s.productId = ?1")
    Integer getTotalStockOutByProductId(int productId);
    
    @Query("SELECT so.id, p.name, so.quantity, so.givenTo, so.outDate " +
           "FROM StockOut so JOIN Product p ON so.productId = p.productId " +
           "WHERE (:productId IS NULL OR so.productId = :productId) " +
           "AND (:startDate IS NULL OR so.outDate >= :startDate) " +
           "AND (:endDate IS NULL OR so.outDate <= :endDate) " +
           "ORDER BY so.outDate DESC")
    List<Object[]> findStockOutWithProductName(@Param("productId") Long productId, 
                                               @Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
}