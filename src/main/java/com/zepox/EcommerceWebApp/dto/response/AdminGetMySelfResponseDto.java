package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminGetMySelfResponseDto {
    private boolean success;
    private String message;
    private String userId;
    private String username;
    private String role;
}
