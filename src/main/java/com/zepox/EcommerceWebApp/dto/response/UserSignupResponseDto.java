package com.zepox.EcommerceWebApp.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignupResponseDto {
    private String username;
    private String userId;
    private boolean success;
    private String jwt;
}
