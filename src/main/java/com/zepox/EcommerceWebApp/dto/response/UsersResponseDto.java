package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersResponseDto {
    private String id;
    private String username;
    private String phoneNumber;
}
