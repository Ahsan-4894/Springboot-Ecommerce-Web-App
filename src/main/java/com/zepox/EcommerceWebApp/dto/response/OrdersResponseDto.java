package com.zepox.EcommerceWebApp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zepox.EcommerceWebApp.entity.type.OrderStatusType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrdersResponseDto {
    private int id;

    private String userId;

    @JsonProperty("Date")
    private LocalDateTime date;

    private OrderStatusType status;

    @JsonProperty("Total")
    private double total;

    private String shippingId;
}
