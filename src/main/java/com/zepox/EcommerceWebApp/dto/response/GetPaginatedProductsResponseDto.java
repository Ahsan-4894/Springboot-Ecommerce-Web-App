package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPaginatedProductsResponseDto {
    private boolean success;
    private List<ProductsResponseDto> message;
    private long totalPages;
    private long totalProducts;
}
