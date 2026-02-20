package com.zest.productapi.service;

import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Integer id);

    ProductResponse updateProduct(Integer id, ProductRequest request);
}