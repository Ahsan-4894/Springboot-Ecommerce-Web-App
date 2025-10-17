package com.zepox.EcommerceWebApp.dto.request;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String username;
    private String password;
}
