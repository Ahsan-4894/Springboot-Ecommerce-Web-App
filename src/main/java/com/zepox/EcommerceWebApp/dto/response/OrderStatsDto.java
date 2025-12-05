package com.zepox.EcommerceWebApp.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderStatsDto {
    private String orderplaced;
    private Long totalOrdersPlaced;
    private Double totalRevenue;
}
