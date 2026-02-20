package com.zest.productapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponse {
    private Integer id;
    private Integer quantity;
}