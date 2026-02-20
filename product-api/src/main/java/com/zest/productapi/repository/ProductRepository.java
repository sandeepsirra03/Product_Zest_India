package com.zest.productapi.repository;

import com.zest.productapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // No code needed – CRUD is auto provided
}
