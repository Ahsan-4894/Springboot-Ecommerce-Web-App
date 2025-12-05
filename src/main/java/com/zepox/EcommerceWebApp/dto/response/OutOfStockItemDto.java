package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutOfStockItemDto {
    private String itemId;
    private int available;
    private int requested;
}
