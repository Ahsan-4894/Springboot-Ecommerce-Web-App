package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLogoutResponseDto {
    private boolean success;
    private String message;
}
