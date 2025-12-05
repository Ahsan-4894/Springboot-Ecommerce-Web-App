package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLogoutResponseDto {
    private boolean success;
    private String message;
}
