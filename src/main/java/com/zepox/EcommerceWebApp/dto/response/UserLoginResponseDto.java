package com.zepox.EcommerceWebApp.dto.response;

import lombok.*;

@Data
@Builder
public class UserLoginResponseDto {
    private String username;
    private String message;
    private String userId;
    private boolean success;
}
