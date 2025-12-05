package com.zepox.EcommerceWebApp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateProductRequestDto(
        @NotBlank(message = "Product ID is required")
        String productId,

        @NotBlank(message = "Category name is required")
        String categoryName,

        @NotBlank(message = "Product name is required")
        String name,

        @Positive(message = "Price must be greater than 0")
        double price,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @NotBlank(message = "Description cannot be blank")
        String description
) {
}
