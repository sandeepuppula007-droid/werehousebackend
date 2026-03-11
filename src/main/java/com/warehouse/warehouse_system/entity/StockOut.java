package com.warehouse.warehouse_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "stock_out")
public class StockOut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private Integer quantity;
    private String givenTo;
    private LocalDateTime outDate;
    private Long productId;
}