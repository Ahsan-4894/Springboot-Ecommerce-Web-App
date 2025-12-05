package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetCategoriesResponseDto {
    private boolean success;
    private List<GetCategoryResponseDto> message;
}
