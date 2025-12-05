package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCategoryResponseDto {
    private int id;
    private String ID;
    private String name;
    private String description;
}
