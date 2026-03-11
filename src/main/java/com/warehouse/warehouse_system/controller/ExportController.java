package com.warehouse.warehouse_system.controller;

import com.warehouse.warehouse_system.repository.ProductRepository;
import com.warehouse.warehouse_system.repository.StockInRepository;
import com.warehouse.warehouse_system.repository.StockOutRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ExportController {

    private final ProductRepository productRepository;
    private final StockInRepository stockInRepository;
    private final StockOutRepository stockOutRepository;

    @GetMapping("/inventory")
    public ResponseEntity<byte[]> exportInventory(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Product Inventory");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Product Name", "Total Stock In", "Total Stock Out", "Available Stock", "Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }
            
            // Get products data
            List<Object[]> products = productRepository.findProductsWithStockSummary();
            
            int rowNum = 1;
            for (Object[] product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue((String) product[1]); // name
                row.createCell(1).setCellValue(((Number) product[2]).longValue()); // totalIn
                row.createCell(2).setCellValue(((Number) product[3]).longValue()); // totalOut
                row.createCell(3).setCellValue(((Number) product[4]).longValue()); // currentStock
                row.createCell(4).setCellValue(((Number) product[4]).longValue() > 0 ? "Available" : "Out of Stock");
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", "inventory-" + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx");
            
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(outputStream.toByteArray());
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stock-history")
    public ResponseEntity<byte[]> exportStockHistory(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock History");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Product Name", "Type", "Quantity", "Party", "Date", "Reference"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }
            
            int rowNum = 1;
            
            // Convert LocalDate to LocalDateTime for comparison
            LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
            LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;
            
            // Get stock in data
            List<Object[]> stockInData = stockInRepository.findStockInWithProductName(productId, startDateTime, endDateTime);
            for (Object[] stock : stockInData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue((String) stock[1]); // product name
                row.createCell(1).setCellValue("Stock In");
                row.createCell(2).setCellValue(((Number) stock[2]).intValue()); // quantity
                row.createCell(3).setCellValue((String) stock[3]); // receivedFrom
                row.createCell(4).setCellValue(stock[4] != null ? stock[4].toString() : ""); // inDate
                row.createCell(5).setCellValue(""); // reference
            }
            
            // Get stock out data
            List<Object[]> stockOutData = stockOutRepository.findStockOutWithProductName(productId, startDateTime, endDateTime);
            for (Object[] stock : stockOutData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue((String) stock[1]); // product name
                row.createCell(1).setCellValue("Stock Out");
                row.createCell(2).setCellValue(((Number) stock[2]).intValue()); // quantity
                row.createCell(3).setCellValue((String) stock[3]); // givenTo
                row.createCell(4).setCellValue(stock[4] != null ? stock[4].toString() : ""); // outDate
                row.createCell(5).setCellValue(""); // reference
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", "stock-history-" + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx");
            
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(outputStream.toByteArray());
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}