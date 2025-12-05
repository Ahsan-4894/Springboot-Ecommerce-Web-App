package com.zepox.EcommerceWebApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiveFeedbackResponseDto {
    private boolean success;
    private String message;
}
