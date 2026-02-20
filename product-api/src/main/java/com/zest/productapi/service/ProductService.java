package com.zest.productapi.service;

import com.zest.productapi.dto.ItemResponse;
import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.dto.ProductResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProductById(Integer id);

    List<ItemResponse> getItemsByProductId(Integer id);

    ProductResponse updateProduct(Integer id, ProductRequest request);

    void deleteProductById(Integer id);
}