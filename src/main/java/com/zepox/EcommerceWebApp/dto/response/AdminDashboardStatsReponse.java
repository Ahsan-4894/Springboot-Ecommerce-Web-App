package com.zepox.EcommerceWebApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardStatsReponse {
    private boolean success;
    private List<Last7DaysOrdersResponseDto> message;
    private Double totalRevnue;
    private List<Double> recent7DaysRevenue;
}
