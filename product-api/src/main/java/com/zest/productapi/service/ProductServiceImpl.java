package com.zest.productapi.service;

import com.zest.productapi.dto.*;
import com.zest.productapi.entity.Item;
import com.zest.productapi.entity.Product;
import com.zest.productapi.exception.ProductNotFoundException;
import com.zest.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = Product.builder()
                .productName(request.getProductName())
                .createdBy(request.getCreatedBy())
                .createdOn(LocalDateTime.now())
                .build();

        List<Item> items = request.getItems().stream()
                .map(i -> Item.builder()
                        .quantity(i.getQuantity())
                        .product(product)
                        .build())
                .collect(Collectors.toList());

        product.setItems(items);

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        return mapToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Integer id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        product.setProductName(request.getProductName());
        product.setModifiedBy(request.getCreatedBy());
        product.setModifiedOn(LocalDateTime.now());

        product.getItems().clear();

        request.getItems().forEach(i ->
                product.getItems().add(
                        Item.builder()
                                .quantity(i.getQuantity())
                                .product(product)
                                .build()
                )
        );

        return mapToResponse(productRepository.save(product));
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .createdBy(product.getCreatedBy())
                .createdOn(product.getCreatedOn())
                .items(product.getItems().stream()
                        .map(i -> ItemResponse.builder()
                                .id(i.getId())
                                .quantity(i.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}