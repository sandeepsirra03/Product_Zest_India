package com.zest.productapi.service;

import com.zest.productapi.dto.ItemRequest;
import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.dto.ProductResponse;
import com.zest.productapi.entity.Product;
import com.zest.productapi.exception.ProductNotFoundException;
import com.zest.productapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1)
                .productName("Test Product")
                .createdBy("admin")
                .createdOn(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();

        productRequest = ProductRequest.builder()
                .productName("Test Product Request")
                .createdBy("admin")
                .items(List.of(new ItemRequest(10)))
                .build();
    }

    @Test
    void createProduct_ShouldReturnProductResponse() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(productRequest);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getProductName(), response.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProductResponse() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productPage);

        Page<ProductResponse> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(product.getProductName(), result.getContent().get(0).getProductName());
    }

    @Test
    void getProductById_WhenExists_ShouldReturnProductResponse() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Test Product", response.getProductName());
    }

    @Test
    void getProductById_WhenDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99));
    }

    @Test
    void deleteProductById_WhenExists_ShouldDeleteProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProductById(1);

        verify(productRepository, times(1)).delete(product);
    }
}
