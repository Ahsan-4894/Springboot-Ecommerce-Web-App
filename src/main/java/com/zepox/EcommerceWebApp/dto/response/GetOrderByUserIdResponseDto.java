package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetOrderByUserIdResponseDto {
    private boolean success;
    OrdersResponseDto message;
}
