package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageUploadServiceResponseDto {
    private final String publicId;
    private final String imageUrl;
}
