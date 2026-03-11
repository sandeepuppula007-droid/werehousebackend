package com.warehouse.warehouse_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "stock_in")
public class StockIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private Integer quantity;
    private String receivedFrom;
    private LocalDateTime inDate;
    private Long productId;
}