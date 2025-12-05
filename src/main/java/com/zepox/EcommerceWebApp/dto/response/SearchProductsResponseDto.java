package com.zepox.EcommerceWebApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductsResponseDto implements Serializable {
    private boolean success;
    private List<ProductsResponseDto> message;
}
