package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateProductResponseDto {
    private final boolean success;
    private final String message;
}
