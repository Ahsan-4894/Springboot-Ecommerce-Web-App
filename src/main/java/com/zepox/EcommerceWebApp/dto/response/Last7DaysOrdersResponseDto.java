package com.zepox.EcommerceWebApp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Last7DaysOrdersResponseDto {
    @JsonProperty(value = "Date")
    private Date orderDate;

    @JsonProperty(value = "TotalOrders")
    private int totalOrders;

    @JsonProperty(value = "Revenue")
    private Double totalRevenue;
}
