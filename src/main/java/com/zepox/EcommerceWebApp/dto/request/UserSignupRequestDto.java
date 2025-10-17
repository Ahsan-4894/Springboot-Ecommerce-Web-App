package com.zepox.EcommerceWebApp.dto.request;


import lombok.Data;

@Data
public class UserSignupRequestDto {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
}
