package com.zest.productapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProductResponse {

    private Integer id;
    private String productName;
    private String createdBy;
    private LocalDateTime createdOn;
    private List<ItemResponse> items;
}