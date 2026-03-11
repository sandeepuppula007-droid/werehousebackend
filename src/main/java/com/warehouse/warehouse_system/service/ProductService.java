package com.warehouse.warehouse_system.service;

import com.warehouse.warehouse_system.entity.Product;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(int id);
    Product createProduct(Product product);
    Product updateProduct(int id, Product product);
    void deleteProduct(int id);
    List<Map<String, Object>> getProductsWithStats();
    Map<String, Object> getProductsWithStatsPaginated(int page, int size, String search);
}