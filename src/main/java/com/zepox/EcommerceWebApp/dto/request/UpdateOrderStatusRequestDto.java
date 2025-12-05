package com.zepox.EcommerceWebApp.dto.request;

import com.zepox.EcommerceWebApp.entity.type.OrderStatusType;

public record UpdateOrderStatusRequestDto(String orderId, OrderStatusType status) {
}
