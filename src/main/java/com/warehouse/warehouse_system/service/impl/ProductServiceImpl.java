package com.warehouse.warehouse_system.service.impl;

import com.warehouse.warehouse_system.entity.Product;
import com.warehouse.warehouse_system.repository.ProductRepository;
import com.warehouse.warehouse_system.repository.StockInRepository;
import com.warehouse.warehouse_system.repository.StockOutRepository;
import com.warehouse.warehouse_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StockInRepository stockInRepository;
    
    @Autowired
    private StockOutRepository stockOutRepository;
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(int id, Product product) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isPresent()) {
            Product existingProduct = existing.get();
            existingProduct.setName(product.getName());
            return productRepository.save(existingProduct);
        }
        return null;
    }
    
    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
    
    @Override
    public List<Map<String, Object>> getProductsWithStats() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapProductWithStats)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getProductsWithStatsPaginated(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        
        if (search != null && !search.trim().isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        
        List<Map<String, Object>> products = productPage.getContent().stream()
                .map(this::mapProductWithStats)
                .collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", products);
        result.put("totalElements", productPage.getTotalElements());
        result.put("totalPages", productPage.getTotalPages());
        result.put("currentPage", page);
        result.put("size", size);
        
        return result;
    }
    
    private Map<String, Object> mapProductWithStats(Product product) {
        Integer totalIn = stockInRepository.getTotalStockInByProductId(product.getProductId());
        Integer totalOut = stockOutRepository.getTotalStockOutByProductId(product.getProductId());
        int currentStock = (totalIn != null ? totalIn : 0) - (totalOut != null ? totalOut : 0);
        
        Map<String, Object> result = new HashMap<>();
        result.put("productId", product.getProductId());
        result.put("name", product.getName());
        result.put("totalIn", totalIn != null ? totalIn : 0);
        result.put("totalOut", totalOut != null ? totalOut : 0);
        result.put("currentStock", currentStock);
        result.put("status", currentStock > 0 ? "Available" : "Out of Stock");
        
        return result;
    }
}