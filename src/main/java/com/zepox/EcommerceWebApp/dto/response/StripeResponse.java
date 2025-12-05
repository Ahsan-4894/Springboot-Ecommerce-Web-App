package com.zepox.EcommerceWebApp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StripeResponse {
    private String message;
    private String sessionId;
    private String sessionUrl;
}
