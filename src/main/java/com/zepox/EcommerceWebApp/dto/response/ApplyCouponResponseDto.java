package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyCouponResponseDto {
    private boolean success;
    private String message;
    private String discountRate;
}
