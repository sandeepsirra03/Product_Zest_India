package com.zest.productapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name cannot be empty")
    private String productName;

    @NotBlank(message = "Created by cannot be empty")
    private String createdBy;

    @NotEmpty(message = "Items list cannot be empty")
    private List<ItemRequest> items;
}